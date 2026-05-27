package com.example.learning.service.implement;

import com.example.learning.dto.request.OderItemRequestDTO;
import com.example.learning.entity.Inventory;
import com.example.learning.entity.Invoice;
import com.example.learning.entity.OderItem;
import com.example.learning.entity.Product;
import com.example.learning.enums.InvoiceStatus;
import com.example.learning.enums.OderStatus;
import com.example.learning.repository.InventoryRepository;
import com.example.learning.repository.InvoiceRepository;
import com.example.learning.repository.OderItemRepository;
import com.example.learning.repository.ProductRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning.dto.request.OderRequestDTO;
import com.example.learning.dto.response.OderResponseDTO;
import com.example.learning.entity.Oder;
import com.example.learning.mapper.OderMapper;
import com.example.learning.repository.OderRepository;
import com.example.learning.service.OderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OderServiceImpl implements OderService {
  private final OderRepository oderRepository;
  private final OderMapper oderMapper;
  private final OderItemRepository oderItemRepository;
  private final ProductRepository productRepository;
  private final InvoiceRepository invoiceRepository;
  private final InventoryRepository inventoryRepository;

  @Override
  public OderResponseDTO getOderId(UUID id) {
    return oderRepository.findById(id)
        .map(oderMapper::toResponse)
        .orElseThrow(()-> new RuntimeException("id not found"));
  }

  @Override
  public OderResponseDTO create(OderRequestDTO oderRequestDTO) {
    Oder oder = oderMapper.toEntity(oderRequestDTO);

    Oder saved = oderRepository.save(oder);
    return oderMapper.toResponse(saved);
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
  public void createOrderItem(OderRequestDTO request) {
    if(request.getItems() == null || request.getItems().isEmpty())
      throw new RuntimeException("order must have items");

    Oder oder = new Oder();
    oder.setUserId(request.getUserId());
    oder.setOderStatus(OderStatus.PENDING);

    oderRepository.save(oder);

    for(OderItemRequestDTO items : request.getItems()){
      OderItem item = new OderItem();
      item.setOrderId(oder.getOderId());
      item.setProductId(items.getProductId());
      item.setQuantity(items.getQuantity());

      Product product = productRepository.findById(item.getProductId())
              .orElseThrow();

      item.setUnitPrice(product.getPrice());
      oderItemRepository.save(item);
    }
  }

  @Override
  @Transactional
  public OderResponseDTO payOrder(UUID id) {
    // 1. Tìm Order
    Oder oder = oderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Order not found"));

    // 2. Chỉ cho phép thanh toán đơn đang PENDING
    if (oder.getOderStatus() != OderStatus.PENDING) {
      throw new RuntimeException("Only PENDING orders can be PAID");
    }

    // 3. Tìm Invoice
    Invoice invoice = invoiceRepository.findByOrderId(oder.getOderId())
        .orElseThrow(() -> new RuntimeException("Invoice not found"));

    // 4. Không cho thanh toán lại
    if (invoice.getInvoiceStatus() == InvoiceStatus.PAID) {
      throw new RuntimeException("Invoice already paid");
    }

    // 5. Cập nhật Invoice
    invoice.setInvoiceStatus(InvoiceStatus.PAID);
    invoiceRepository.save(invoice);

    // 6. Cập nhật Order
    oder.setOderStatus(OderStatus.PAID);
    Oder saved = oderRepository.save(oder);

    // 7. Trả response
    return oderMapper.toResponse(saved);
  }

  private static final Set<OderStatus> NOT_CANCELABLE_STATUS =
      Set.of(
          OderStatus.DELIVERING,
          OderStatus.COMPLETED
      );

  @Override
  @Transactional
  public OderResponseDTO cancelOder(UUID id){
    Oder oder = getOrderById(id);

    validateCancelable(oder);

    cancelOrderStatus(oder);

    restoreInventory(id);

    cancelInvoice(oder);

    saveOrder(oder);

    return mapToResponse(oder);
  }

  private Oder getOrderById(UUID orderId) {

    return oderRepository.findById(orderId)
        .orElseThrow(() ->
            new RuntimeException("Order not found")
        );
  }

  private void validateCancelable(Oder order) {

    if (NOT_CANCELABLE_STATUS.contains(order.getOderStatus())) {

      throw new RuntimeException(
          "Cannot cancel order in status: "
              + order.getOderStatus()
      );
    }
  }

  private void cancelOrderStatus(Oder order) {

    order.setOderStatus(OderStatus.CANCELLED);
  }

  private void restoreInventory(UUID orderId) {
    List<OderItem> orderItems =
        oderItemRepository.findByOrderId(orderId);

    orderItems.forEach(item -> {
      Inventory inventory =
          inventoryRepository
              .findByProductId(item.getProductId())
              .orElseThrow(() ->
                  new RuntimeException(
                      "Inventory not found"
                  )
              );
      inventory.setQuantity(
          inventory.getQuantity()
              + item.getQuantity().intValue()
      );
      inventoryRepository.save(inventory);
    });
  }

  private void cancelInvoice(Oder order) {

    Invoice invoice =
        invoiceRepository.findByOrderId(
            order.getOderId()
        ).orElseThrow(() ->
            new RuntimeException(
                "Invoice not found"
            )
        );
    invoice.setInvoiceStatus(InvoiceStatus.UNPAID);
    invoiceRepository.save(invoice);
  }

  private void saveOrder(Oder order) {
    oderRepository.save(order);
  }

  private OderResponseDTO mapToResponse(Oder oder){
    return oderMapper.toResponse(oder);
  }


  public void updateStatus(UUID uuid, OderStatus oderStatus){
    Oder oder = oderRepository.findById(uuid)
        .orElseThrow(() -> new RuntimeException("order not found"));

    OderStatus oderStatus1 = oder.getOderStatus();

    if(oderStatus1 == OderStatus.PENDING && oderStatus != OderStatus.CONFIRMED
                                         && oderStatus != OderStatus.CANCELLED){
      throw new RuntimeException("PENDING chỉ được chuyển sang CONFIRMED or CANCELLED");
    }

    if(oderStatus1 == OderStatus.CONFIRMED && oderStatus != OderStatus.DELIVERING
                                           && oderStatus != OderStatus.CANCELLED){
      throw new RuntimeException("CONFIRMED chỉ được chuyển sang DELIVERING or CANCELLED");
    }

    if(oderStatus1 == OderStatus.DELIVERING && oderStatus != OderStatus.COMPLETED){
      throw new RuntimeException("DELIVERING chỉ chuyển sang COMPLETED");
    }

    oder.setOderStatus(oderStatus);

    oderRepository.save(oder);
  }
}
