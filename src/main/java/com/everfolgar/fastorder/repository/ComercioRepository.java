package com.everfolgar.fastorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.everfolgar.fastorder.model.CategoriaComercio;
import com.everfolgar.fastorder.model.Comercio;

public interface ComercioRepository extends JpaRepository<Comercio, Long> {
    List<Comercio> findByAbiertoTrue();
    List<Comercio> findByAbiertoTrueAndCategoria(CategoriaComercio categoria);
}
