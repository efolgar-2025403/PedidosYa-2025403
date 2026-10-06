package com.everfolgar.fastorder.dto;

import com.everfolgar.fastorder.model.CategoriaComercio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComercioRequest {
    @NotBlank private String nombre;
    @NotNull private CategoriaComercio categoria;
    private String direccion;
    private boolean abierto;
}
