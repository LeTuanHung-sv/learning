package com.example.learning.repository;

import com.example.learning.entity.Invoice;
import com.example.learning.entity.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
  Optional<Invoice> findByOderId(UUID oderId);

}
