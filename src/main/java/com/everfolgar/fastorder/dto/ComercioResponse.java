package com.everfolgar.fastorder.dto;

import com.everfolgar.fastorder.model.CategoriaComercio;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComercioResponse {
    private Long id;
    private String nombre;
    private CategoriaComercio categoria;
    private String direccion;
    private boolean abierto;
}
