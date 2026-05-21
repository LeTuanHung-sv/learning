package com.example.learning.service.implement;

import com.example.learning.dto.request.OderItemRequestDTO;
import com.example.learning.dto.response.OderItemResponse;
import com.example.learning.entity.Inventory;
import com.example.learning.entity.Invoice;
import com.example.learning.entity.OderItem;
import com.example.learning.entity.Product;
import com.example.learning.entity.User;
import com.example.learning.enums.InvoiceStatus;
import com.example.learning.enums.OderStatus;
import com.example.learning.enums.ProductStatus;
import com.example.learning.repository.InventoryRepository;
import com.example.learning.repository.InvoiceRepository;
import com.example.learning.repository.OderItemRepository;
import com.example.learning.repository.ProductRepository;
import com.example.learning.repository.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
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
  private final UserRepository userRepository;
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

  @Override
  public OderResponseDTO cancelOrder(UUID id){
    Oder oder = oderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Order not found"));

    if(oder.getOderStatus() == OderStatus.PAID)
      throw new RuntimeException("Paid order cannot be cancelled");

    if(oder.getOderStatus() == OderStatus.CANCELLED)
      throw new RuntimeException("Order already cancelled");

    oder.setOderStatus(OderStatus.CANCELLED);

    Oder saved = oderRepository.save(oder);

    return oderMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public OderResponseDTO placeOrder(OderRequestDTO oderRequestDTO){
    BigDecimal totalAmount = BigDecimal.ZERO;

    List<OderItem> oderItemArrayList = new ArrayList<>();

    Oder oder = oderMapper.toEntity(oderRequestDTO);
    oder.setOderStatus(OderStatus.PENDING);
    oder = oderRepository.save(oder);

    User user = userRepository.findById(oderRequestDTO.getUserId())
        .orElseThrow(()-> new RuntimeException("User not found"));

    if(oderRequestDTO.getItems() == null || oderRequestDTO.getItems().isEmpty())
      throw new RuntimeException("Items cannot be empty");

    for(OderItemRequestDTO oderItemRequestDTO : oderRequestDTO.getItems()){
      Product product = productRepository.findById(oderItemRequestDTO.getProductId())
          .orElseThrow(()-> new RuntimeException("product not found"));

      if(product.getProductStatus() != ProductStatus.Active)
        throw new RuntimeException("product is not active");

      Inventory inventory = inventoryRepository.findByProductId(product.getProductId())
          .orElseThrow(()-> new RuntimeException("Inventory not found"));

      if(inventory.getQuantity() < oderItemRequestDTO.getQuantity().intValue())
        throw new RuntimeException("not enough stock");

      OderItem oderItem = new OderItem();
      oderItem.setOrderId(oder.getOderId());
      oderItem.setProductId(product.getProductId());
      oderItem.setQuantity(oderItemRequestDTO.getQuantity());
      oderItem.setUnitPrice(product.getPrice());
      BigDecimal subtotal = product.getPrice().multiply(oderItemRequestDTO.getQuantity());
      oderItem.setSubtotal(subtotal);

      oderItemArrayList.add(oderItem);
      totalAmount = totalAmount.add(subtotal);

      inventory.setQuantity(inventory.getQuantity() - oderItemRequestDTO.getQuantity().intValue());
      inventoryRepository.save(inventory);
    }

    oderItemRepository.saveAll(oderItemArrayList);

    oder.setTotalAmount(totalAmount);
    oderRepository.save(oder);

    Invoice invoice = new Invoice();
    invoice.setOderId(oder.getOderId());
    invoice.setTotalAmount(totalAmount);
    invoice.setInvoiceStatus(InvoiceStatus.UNPAID);
    invoiceRepository.save(invoice);

    return oderMapper.toResponse(oder);

  }
}
