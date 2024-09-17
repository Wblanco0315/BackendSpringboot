package com.example.transacciones.dto.sesion;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username","message","jwt","success"})
public record AuthResponse(String username,
                           String message,
                           String token,
                           boolean status) {
}
