package com.example.transacciones.models.transactions;

import com.example.transacciones.models.enums.TransactionStatus;
import com.example.transacciones.models.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "transactions")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransaccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "cedula",nullable = false, length = 20)
    private String cedula;

    @Column(name = "account_number", length = 20)
    private String accountNumber;

    @Column(name = "destination_account")
    private String destinationAccount; // Destination account number for transfers

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'completed'")
    private TransactionStatus status;

    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
