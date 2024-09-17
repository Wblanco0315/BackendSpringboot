package com.example.transacciones.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"message","success"})
public record ResponseMessage(
       String menssage,
       boolean success
) {
}
