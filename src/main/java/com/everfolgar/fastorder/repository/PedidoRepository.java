package com.everfolgar.fastorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.everfolgar.fastorder.model.EstadoPedido;
import com.everfolgar.fastorder.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByEstadoIn(List<EstadoPedido> estados);
    List<Pedido> findByRepartidorId(Long repartidorId);
}
