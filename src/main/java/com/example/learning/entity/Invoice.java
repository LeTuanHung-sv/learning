package com.example.learning.entity;

import com.example.learning.enums.InvoiceStatus;
import com.example.learning.enums.PaymentMethod;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class Invoice extends Auditable{
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID invoiceId;
    private UUID oderId;
    private BigDecimal invoiceNumber;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private InvoiceStatus invoiceStatus;
}
