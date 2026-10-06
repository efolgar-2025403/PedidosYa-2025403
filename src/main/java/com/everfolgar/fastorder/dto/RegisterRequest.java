package com.everfolgar.fastorder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank private String nombre;
    private String direccion;
    private String telefono;
    @Email @NotBlank private String email;
    @NotBlank private String password;
}
