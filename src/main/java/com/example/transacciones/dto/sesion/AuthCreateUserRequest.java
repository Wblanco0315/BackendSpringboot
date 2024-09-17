package com.example.transacciones.dto.sesion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
        @NotBlank String name,
        @NotBlank @Valid String cedula,
        @NotBlank @Email String email,
        @NotBlank @Valid String password
) {
}
