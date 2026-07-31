package com.example.learning.service.implement;

import com.example.learning.dto.request.OderItemRequestDTO;
import com.example.learning.entity.Inventory;
import com.example.learning.entity.Invoice;
import com.example.learning.entity.OderItem;
import com.example.learning.entity.Product;
import com.example.learning.enums.InvoiceStatus;
import com.example.learning.enums.OrderStatus;
import com.example.learning.enums.PaymentMethod;
import com.example.learning.enums.ProductStatus;
import com.example.learning.exception.BusinessException;
import com.example.learning.exception.ResourceNotFoundException;
import com.example.learning.repository.InventoryRepository;
import com.example.learning.repository.InvoiceRepository;
import com.example.learning.repository.OderRepository;
import com.example.learning.repository.OrderItemRepository;
import com.example.learning.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.hibernate.query.Order;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning.dto.request.OderRequestDTO;
import com.example.learning.dto.response.OderResponseDTO;
import com.example.learning.entity.Oder;
import com.example.learning.mapper.OderMapper;
import com.example.learning.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OderRepository oderRepository;
  private final OderMapper oderMapper;
  private final OrderItemRepository oderItemRepository;
  private final ProductRepository productRepository;
  private final InvoiceRepository invoiceRepository;
  private final InventoryRepository inventoryRepository;

  @Override
  public OderResponseDTO getOderId(UUID id) {
    return oderRepository.findById(id)
        .map(oderMapper::toResponse)
        .orElseThrow(()-> new ResourceNotFoundException("id not found"));
  }

  @Override
  @Transactional
  public OderResponseDTO create(OderRequestDTO oderRequestDTO) {


    // vì: phải validate trước nếu không thì lỡ KH mua sp không tồn tại, không còn hdong nưữa
    validateProducts(oderRequestDTO);
    // vì: phải validate kho lỡ KH mua sluon nhieu hơn trong kho -> quăng lỗi
    validateInventory(oderRequestDTO);

    Oder order = createOrder(oderRequestDTO);

    createOrderItems(order, oderRequestDTO);
    // vì nếu khách mua hang ròi thì trừ kho không trừ khách sau mua nhiều < sluong kho cũ và > sluong kho moi --> lỗi
    reserveInventory(oderRequestDTO);

    createInvoice(order, oderRequestDTO.getPaymentMethod());

    return oderMapper.toResponse(order);
  }

  @Override
  public List<OderResponseDTO> getAllOder() {
    return oderRepository.findAll()
        .stream()
        .map(oderMapper::toResponse)
        .toList();
  }


  @Override
  @Transactional
  public OderResponseDTO payOrder(UUID id) {
    Oder oder = oderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    if (oder.getOderStatus() != OrderStatus.PENDING && oder.getOderStatus() != OrderStatus.CONFIRMED) {
      throw new BusinessException("Only PENDING or CONFIRMED orders can be PAID");
    }

    Invoice invoice = invoiceRepository.findByOderId(oder.getOderId())
        .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

    if (invoice.getInvoiceStatus() == InvoiceStatus.PAID) {
      throw new BusinessException("Invoice already paid");
    }

    invoice.setInvoiceStatus(InvoiceStatus.PAID);
    invoiceRepository.save(invoice);

    Oder saved = oderRepository.save(oder);

    return oderMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public OderResponseDTO cancelOrder(UUID id){
    // phải check lỡ không có order -> lỗi
    Oder oder = oderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    if (oder.getOderStatus() != OrderStatus.PENDING
        && oder.getOderStatus() != OrderStatus.CONFIRMED) {

      throw new BusinessException(
          "Only PENDING or CONFIRMED orders can be cancelled");
    }

    List<OderItem> orderItems =
        oderItemRepository.findByOderId(id);

    for (OderItem item : orderItems) {

      Inventory inventory = inventoryRepository
          .findByProductId(item.getProductId())
          .orElseThrow(() ->
              new ResourceNotFoundException(
                  "Inventory not found"));

      inventory.setQuantity(
          inventory.getQuantity().add(item.getQuantity())
      );
    }
    oder.setOderStatus(OrderStatus.CANCELLED);
    return oderMapper.toResponse(oder);
  }

  @Override
  @Transactional
  public void updateStatus(UUID uuid, OrderStatus oderStatus  ) {// oderStatus = Delivering
    // tìm kiếm order -- tại sao phải tìm
    // vì muốn update order đó thì order đó phải tồn tại nếu không tồn tại -> lỗi
    Oder oder = oderRepository.findById(uuid)
        .orElseThrow(() -> new ResourceNotFoundException("order not found"));
    // lấy status ở DB -- tại sao
    // phải lấy ở DB để biết chuyển status có hợp với logic nghiệp vụ không
    OrderStatus currentStatus = oder.getOderStatus(); // CONFIRMED

    // nếu ở trạng thái COMPLETED -> quăng Exception
    if (currentStatus == OrderStatus.COMPLETED) {
      throw new BusinessException("Order is already completed");
    }

    // nếu ở trạng thái CANCELLED -> quăng Exception
    if (currentStatus == OrderStatus.CANCELLED) {
      throw new BusinessException("Order is already cancelled");
    }

    switch (currentStatus) {
      case PENDING:
        // nếu orderStatus nó != CONFIRMED và != CANCELLED -> quăng lỗi
        // nếu giống thì xuống dưới setStatus rồi lưu DB
        if (oderStatus != OrderStatus.CONFIRMED
            && oderStatus != OrderStatus.CANCELLED) {
          throw new BusinessException(
              "PENDING chỉ được chuyển sang CONFIRMED hoặc CANCELLED");
        }
        break;

      case CONFIRMED:
        // nếu nó là DELIVERING thì nó sẽ tìm xem có hóa đơn kh
        if (oderStatus == OrderStatus.DELIVERING) {
          Invoice invoice = invoiceRepository.findByOderId(oder.getOderId())
              .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
          // hóa đơn chưa được xử lý thì quăng exception
          if (invoice.getInvoiceStatus() != InvoiceStatus.PAID) {
            throw new BusinessException("order Must be paid before delivering");
          }
          // nếu nó != DELIVERING thì check phải CANCELLED kh
          // nếu kh phải -> quăng lỗi
          // nếu phải -> setStatus CANCELLED -> lưu DB
        } else if (oderStatus != OrderStatus.CANCELLED) {
          throw new BusinessException(
              "CONFIRMED chỉ được chuyển sang DELIVERING hoặc CANCELLED");
        }

        break;

      case DELIVERING:

        if (oderStatus != OrderStatus.COMPLETED) {
          throw new BusinessException(
              "DELIVERING chỉ được chuyển sang COMPLETED");
        }

        break;

      default:
        throw new BusinessException("Invalid order status");
    }

    oder.setOderStatus(oderStatus);
    oderRepository.save(oder);
  }

  private void validateProducts(OderRequestDTO requestDTO){
    for(OderItemRequestDTO item : requestDTO.getItems()) {
      Product product = productRepository.findById(item.getProductId())
          .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
      if (product.getProductStatus() != ProductStatus.Active)
        throw new BusinessException("Product is inactive");
    }
  }
  // trong method này có sử dụng hashMap vì
  // HashMap giúp gôm lại tổng số lượng mà KH muốn order
  // merge dùng để ộng dồn dữ liệu lại thay vì if else nhiều lần
  private void validateInventory(OderRequestDTO requestDTO)
  {
    // tạo HashMap totalQuantity dùng để lưu productId = key và tổng quantity = value
    Map<UUID, BigDecimal> totalQuantity = new HashMap<>();
    // duyệt từng Item mà khách hàng chọn
    for (OderItemRequestDTO item : requestDTO.getItems()) {
      // kiểm tra xem trong hashMap có tồn tại productId chưa
      // ch thì thêm mới productId và quantity
      // có rồi thì lấy sluong cũ + thêm sluong moi vào và update giá trị trong hashMap
      totalQuantity.merge(
          item.getProductId(),
          item.getQuantity(),
          BigDecimal::add
      );
    }
    // Duyệt entry qua tất cả các cặp key đang có trong hashMap
    for(Map.Entry<UUID, BigDecimal> entry : totalQuantity.entrySet()){
      Inventory inventory = inventoryRepository.findByProductId(entry.getKey())
          .orElseThrow(() -> new ResourceNotFoundException("inventory not found"));
      // ktra so luong kho
      if(inventory.getQuantity().compareTo(entry.getValue()) < 0) {
        throw new BusinessException(
            "Product " + entry.getKey()
                + " only has "
                + inventory.getQuantity()
                + " items but customer requested "
                + entry.getValue()
        );
      }
    }
  }

  private Oder createOrder(OderRequestDTO requestDTO){
    Oder order = oderMapper.toEntity(requestDTO);
    order.setOderStatus(OrderStatus.PENDING);
    return oderRepository.save(order);
  }

  private void createOrderItems(Oder order, OderRequestDTO requestDTO){
    BigDecimal total = BigDecimal.ZERO;
    for(OderItemRequestDTO itemRequestDTO : requestDTO.getItems()){
      Product product = productRepository.findById(itemRequestDTO.getProductId())
          .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
      // tính tổng giá tiền của các sản phẩm
      BigDecimal subtotal =product.getPrice().multiply(itemRequestDTO.getQuantity());

      OderItem orderItem = OderItem.builder()
          .oderId(order.getOderId())
          .productId(itemRequestDTO.getProductId())
          .quantity(itemRequestDTO.getQuantity())
          .unitPrice(product.getPrice())
          .subtotal(subtotal)
          .build();

      oderItemRepository.save(orderItem);
      // cộng dồn vào tổng tiền của order
      total = total.add(subtotal);
    }
    order.setTotalAmount(total);
    oderRepository.save(order);
  }

  private void reserveInventory(OderRequestDTO requestDTO){
    for(OderItemRequestDTO item : requestDTO.getItems()){
      Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
          .orElseThrow(() -> new ResourceNotFoundException("inventory not found for productId" + item.getProductId()));
      inventory.setQuantity(
          inventory.getQuantity().subtract(item.getQuantity())
      );
      inventoryRepository.save(inventory);
    }
  }

  private void createInvoice(Oder order, PaymentMethod paymentMethod){
    String invoiceNumber ="INV-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
        + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

    Invoice invoice = Invoice.builder()
        .oderId(order.getOderId())
        .invoiceNumber(invoiceNumber)
        .invoiceStatus(InvoiceStatus.UNPAID)
        .totalAmount(order.getTotalAmount())
        .paymentMethod(paymentMethod)
        .build();
    invoiceRepository.save(invoice);
  }
}
