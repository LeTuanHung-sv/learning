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
    validateProducts(oderRequestDTO);

    validateInventory(oderRequestDTO);

    Oder order = createOrder(oderRequestDTO);

    createOrderItems(order, oderRequestDTO);

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
    // 1. Tìm Order
    Oder oder = oderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    // 2. Chỉ cho phép thanh toán đơn đang PENDING
    if (oder.getOderStatus() != OrderStatus.PENDING && oder.getOderStatus() != OrderStatus.CONFIRMED) {
      throw new BusinessException("Only PENDING or CONFIRMED orders can be PAID");
    }

    // 3. Tìm Invoice
    Invoice invoice = invoiceRepository.findByOderId(oder.getOderId())
        .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));


    // 4. Không cho thanh toán lại
    if (invoice.getInvoiceStatus() == InvoiceStatus.PAID) {
      throw new BusinessException("Invoice already paid");
    }

    // 5. Cập nhật Invoice
    invoice.setInvoiceStatus(InvoiceStatus.PAID);
    invoiceRepository.save(invoice);

    Oder saved = oderRepository.save(oder);

    // 7. Trả response
    return oderMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public OderResponseDTO cancelOrder(UUID id){
    Oder oder = oderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

    if(oder.getOderStatus() == OrderStatus.PAID)
      throw new BusinessException("Paid order cannot be cancelled");

    if(oder.getOderStatus() == OrderStatus.CANCELLED)
      throw new BusinessException("Order already cancelled");



    oder.setOderStatus(OrderStatus.CANCELLED);

    Oder saved = oderRepository.save(oder);

    return oderMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public void updateStatus(UUID uuid, OrderStatus oderStatus){
    Oder oder = oderRepository.findById(uuid)
        .orElseThrow(() -> new ResourceNotFoundException("order not found"));

    OrderStatus oderStatus1 = oder.getOderStatus();

    if(oderStatus1 == OrderStatus.PENDING && oderStatus != OrderStatus.CONFIRMED
                                         && oderStatus != OrderStatus.CANCELLED){
      throw new BusinessException("PENDING chỉ được chuyển sang CONFIRMED or CANCELLED");
    }

    if(oderStatus1 == OrderStatus.CONFIRMED && oderStatus != OrderStatus.DELIVERING
                                           && oderStatus != OrderStatus.CANCELLED){
      throw new BusinessException("CONFIRMED chỉ được chuyển sang DELIVERING or CANCELLED");
    }

    if(oderStatus1 == OrderStatus.DELIVERING && oderStatus != OrderStatus.COMPLETED){
      throw new BusinessException("DELIVERING chỉ chuyển sang COMPLETED");
    }

    oder.setOderStatus(oderStatus);

    oderRepository.save(oder);
  }

  @Override
  @Transactional
  public void deleteAll(){
    oderRepository.deleteAll();
  }

  private void validateProducts(OderRequestDTO requestDTO){
    for(OderItemRequestDTO item : requestDTO.getItems()) {
      Product product = productRepository.findById(item.getProductId())
          .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
      if (product.getProductStatus() != ProductStatus.Active)
        throw new BusinessException("Product is inactive");
    }
  }

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
