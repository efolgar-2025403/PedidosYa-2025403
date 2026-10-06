package com.everfolgar.fastorder.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PedidoRequest {

    @NotEmpty
    @Valid
    private List<ItemPedidoRequest> items;

    @Data
    public static class ItemPedidoRequest {
        private Long productoId;
        private Integer cantidad;
    }
}
