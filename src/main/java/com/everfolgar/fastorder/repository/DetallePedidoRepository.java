package com.everfolgar.fastorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.everfolgar.fastorder.model.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}
