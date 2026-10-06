package com.everfolgar.fastorder.dto;

import com.everfolgar.fastorder.model.EstadoPedido;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoPedidoRequest {
    @NotNull
    private EstadoPedido estado;
}
