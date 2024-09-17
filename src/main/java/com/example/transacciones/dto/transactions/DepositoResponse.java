package com.example.transacciones.dto.transactions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"message"})
public record DepositoResponse(String message) {

}
