package com.example.learning.repository;

import com.example.learning.entity.OderItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OderItemRepository extends JpaRepository<OderItem, UUID> {
   List<OderItem> findByOrderId(UUID orderId);
}
