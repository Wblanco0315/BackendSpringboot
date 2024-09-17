package com.example.transacciones.dto.transactions;

import lombok.Data;

@Data
public class DepositoRequest{
    String identifier;
    Long amount;
}
