package com.everfolgar.fastorder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.everfolgar.fastorder.dto.ProductoRequest;
import com.everfolgar.fastorder.dto.ProductoResponse;
import com.everfolgar.fastorder.exception.ResourceNotFoundException;
import com.everfolgar.fastorder.model.Comercio;
import com.everfolgar.fastorder.model.Producto;
import com.everfolgar.fastorder.repository.ComercioRepository;
import com.everfolgar.fastorder.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ComercioRepository comercioRepository;

    public List<ProductoResponse> listarPorComercio(Long comercioId) {
        if (!comercioRepository.existsById(comercioId)) {
            throw new ResourceNotFoundException("Comercio no encontrado: " + comercioId);
        }
        return productoRepository.findByComercioId(comercioId).stream()
                .map(p -> new ProductoResponse(p.getId(), p.getComercio().getId(), p.getNombre(), p.getPrecio(), p.getStock(), p.isDisponible()))
                .toList();
    }

    public ProductoResponse crear(Long comercioId, ProductoRequest req) {
        Comercio comercio = comercioRepository.findById(comercioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comercio no encontrado: " + comercioId));
        Producto p = Producto.builder()
                .comercio(comercio)
                .nombre(req.getNombre())
                .precio(req.getPrecio())
                .stock(req.getStock())
                .disponible(req.isDisponible())
                .build();
        p = productoRepository.save(p);
        return new ProductoResponse(p.getId(), comercioId, p.getNombre(), p.getPrecio(), p.getStock(), p.isDisponible());
    }
}
