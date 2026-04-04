package com.example.learning.repository;

import com.example.learning.entity.Inventory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

}
