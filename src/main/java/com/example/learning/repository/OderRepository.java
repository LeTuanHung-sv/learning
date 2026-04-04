package com.example.learning.repository;

import com.example.learning.entity.Oder;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OderRepository extends JpaRepository<Oder, UUID> {

}
