package com.everfolgar.fastorder.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoResponse {
    private Long id;
    private Long comercioId;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private boolean disponible;
}
