package com.everfolgar.fastorder.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductoRequest {
    @NotBlank private String nombre;
    @DecimalMin("0.0") private BigDecimal precio;
    @Min(0) private Integer stock;
    private boolean disponible;
}
