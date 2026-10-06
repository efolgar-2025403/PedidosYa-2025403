package com.everfolgar.fastorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.everfolgar.fastorder.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByComercioId(Long comercioId);
}
