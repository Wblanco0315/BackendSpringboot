package com.example.transacciones.dto.transactions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"identifierOrigen","identifierDestino","amount"})
public record TransferRequest(String identifierOrigen, String identifierDestino, Double amount){
}
