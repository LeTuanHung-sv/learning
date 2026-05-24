package com.example.learning.repository;

import com.example.learning.entity.Oder;
import com.example.learning.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OderRepository extends JpaRepository<Oder, UUID> {
  List<Oder> findByUser(User user);
}
