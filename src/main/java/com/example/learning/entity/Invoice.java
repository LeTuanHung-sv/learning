package com.example.learning.entity;

import com.example.learning.enums.InvoiceStatus;
import com.example.learning.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.UniqueElements;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Invoice extends Auditable{
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID invoiceId;
    private UUID oderId;
    @Column(unique = true, nullable = false)
    private BigDecimal invoiceNumber;
    @NotNull
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;
}
