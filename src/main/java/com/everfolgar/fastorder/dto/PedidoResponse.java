package com.everfolgar.fastorder.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.everfolgar.fastorder.model.EstadoPedido;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PedidoResponse {
    private Long id;
    private Long clienteId;
    private Long repartidorId;
    private LocalDateTime fechaPedido;
    private BigDecimal costoEnvio;
    private BigDecimal montoTotal;
    private EstadoPedido estado;
    private List<DetallePedidoResponse> detalles;
}
