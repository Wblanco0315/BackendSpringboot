package com.example.transacciones.models.transactions;


import com.example.transacciones.models.enums.TransactionStatus;
import com.example.transacciones.models.enums.TransactionType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder

@Table(name = "audits")
public class AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // Referencia al usuario que realizó la transacción

    @Column(nullable = false, length = 20)
    private String cedula; // Número de identificación del usuario

    @Column(name = "user_name")
    private String userName; // Nombre completo del usuario

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType; // Tipo de transacción

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount; // Monto involucrado en la transacción

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus; // Estado de la transacción

    @Column(name = "account_number", nullable = true)
    private String accountNumber; // Número de cuenta de origen (para transferencias)

    @Column(name = "destination_account", nullable = true)
    private String destinationAccount; // Número de cuenta de destino (para transferencias)

    @CreationTimestamp
    private LocalDateTime timestamp = LocalDateTime.now(); // Marca de tiempo de la transacción

}
