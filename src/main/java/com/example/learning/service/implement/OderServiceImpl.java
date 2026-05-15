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
import com.example.learning.enums.PaymentMethod;
import com.example.learning.enums.ProductStatus;
import com.example.learning.repository.InventoryRepository;
import com.example.learning.repository.InvoiceRepository;
import com.example.learning.repository.OderItemRepository;
import com.example.learning.repository.ProductRepository;
import com.example.learning.repository.UserRepository;
import java.math.BigDecimal;
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
@Transactional
public class OderServiceImpl implements OderService {
  private final OderRepository oderRepository;
  private final OderMapper oderMapper;
  private final OderItemRepository oderItemRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final InventoryRepository inventoryRepository;
  private final InvoiceRepository invoiceRepository;

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

      item.setPrice(product.getPrice());
      oderItemRepository.save(item);
    }
  }

  @Override
  public OderResponseDTO payOrder(UUID id) {
    Oder oder = oderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Order not found"));

    if(oder.getOderStatus() != OderStatus.PENDING)
      throw new RuntimeException("Only PENDING orders can be PAID");

    oder.setOderStatus(OderStatus.PAID);

    return oderMapper.toResponse(oderRepository.save(oder));
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
  public OderResponseDTO createOrder(OderRequestDTO oderRequestDTO) {
    User user = userRepository.findById(oderRequestDTO.getUserId())
        .orElseThrow(() -> new RuntimeException("user not found"));

    if(oderRequestDTO.getItems() == null || oderRequestDTO.getItems().isEmpty()){
      throw new RuntimeException("Items cannot be empty");
    }

    Oder oder = new Oder();
    oder.setUserId(user.getUserId());
    oder.setOderStatus(OderStatus.PENDING);
    oder.setTotalAmount(BigDecimal.ZERO);
    oderRepository.save(oder);

    BigDecimal totalAmount = BigDecimal.ZERO;

    for(OderItemRequestDTO itemRequestDTO : oderRequestDTO.getItems()) {
      Product product = productRepository.findById(itemRequestDTO.getProductId())
          .orElseThrow(() -> new RuntimeException("Product not found"));

      if (product.getProductStatus() != ProductStatus.Active)
        throw new RuntimeException("Product is not active");

      Inventory inventory = inventoryRepository.findByProductId(product.getProductId())
          .orElseThrow(() -> new RuntimeException("Inventory not found"));
      if (inventory.getQuantity() < itemRequestDTO.getQuantity().intValue()) {
        throw new RuntimeException("not enough stock");
      }

      inventory.setQuantity(
          inventory.getQuantity() - itemRequestDTO.getQuantity().intValue()
      );
      inventoryRepository.save(inventory);

      OderItem oderItem = new OderItem();
      oderItem.setOrderId(oder.getOderId());
      oderItem.setProductId(product.getProductId());
      oderItem.setQuantity(itemRequestDTO.getQuantity());
      oderItem.setPrice(product.getPrice());
      oderItemRepository.save(oderItem);

      BigDecimal itemTotal = product.getPrice().multiply(itemRequestDTO.getQuantity());

      totalAmount = totalAmount.add(itemTotal);
    }

      oder.setTotalAmount(totalAmount);
      oder = oderRepository.save(oder);

      Invoice invoice = new Invoice();
      invoice.setOderId(oder.getOderId());
      invoice.setInvoiceNumber(BigDecimal.valueOf(System.currentTimeMillis()));
      invoice.setTotalAmount(totalAmount);
      invoice.setPaymentMethod(PaymentMethod.CASH);
      invoice.setInvoiceStatus(InvoiceStatus.UNPAID);
      invoice = invoiceRepository.save(invoice);

      return new OderResponseDTO(
          oder.getOderId(),
          oder.getUserId(),
          totalAmount,
          oder.getOderStatus(),
          invoice.getInvoiceStatus()
      );
    }
  }
