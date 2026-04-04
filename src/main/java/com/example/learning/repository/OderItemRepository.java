package com.example.learning.repository;

import com.example.learning.entity.OderItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OderItemRepository extends JpaRepository<OderItem, UUID> {

}
