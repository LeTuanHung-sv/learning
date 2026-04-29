package com.example.learning.service.implement;

import com.example.learning.dto.request.OderItemRequestDTO;
import com.example.learning.dto.response.OderItemResponse;
import com.example.learning.entity.OderItem;
import com.example.learning.entity.Product;
import com.example.learning.enums.OderStatus;
import com.example.learning.repository.OderItemRepository;
import com.example.learning.repository.ProductRepository;
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
public class OderServiceImpl implements OderService {
  private final OderRepository oderRepository;
  private final OderMapper oderMapper;
  private final OderItemRepository oderItemRepository;
  private final ProductRepository productRepository;

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


}
