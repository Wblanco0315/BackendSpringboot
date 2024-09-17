package com.example.transacciones.dto.sesion;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public record AuthCreateRoleRequest(@Size(max = 2,message = "El usuario no puede tener mas de 2 roles")
                                    List<String> roleListName) {
}
