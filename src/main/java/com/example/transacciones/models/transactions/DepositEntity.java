package com.example.transacciones.models.transactions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "deposits")
public class DepositEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 20)
    private String cedula; // Cédula del usuario, permite valores nulos

    @Column(name = "account_number", length = 20)
    private String accountNumber; // Número de cuenta del usuario, permite valores nulos

    @Column(name = "timestamp")
    @CreationTimestamp
    private LocalDateTime timestamp;
}
