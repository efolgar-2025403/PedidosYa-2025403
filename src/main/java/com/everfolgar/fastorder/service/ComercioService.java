package com.everfolgar.fastorder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.everfolgar.fastorder.dto.ComercioRequest;
import com.everfolgar.fastorder.dto.ComercioResponse;
import com.everfolgar.fastorder.model.CategoriaComercio;
import com.everfolgar.fastorder.model.Comercio;
import com.everfolgar.fastorder.repository.ComercioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComercioService {

    private final ComercioRepository comercioRepository;

    public List<ComercioResponse> listar(CategoriaComercio categoria) {
        List<Comercio> comercios = (categoria == null)
                ? comercioRepository.findByAbiertoTrue()
                : comercioRepository.findByAbiertoTrueAndCategoria(categoria);
        return comercios.stream()
                .map(c -> new ComercioResponse(c.getId(), c.getNombre(), c.getCategoria(), c.getDireccion(), c.isAbierto()))
                .toList();
    }

    public ComercioResponse crear(ComercioRequest req) {
        Comercio c = Comercio.builder()
                .nombre(req.getNombre())
                .categoria(req.getCategoria())
                .direccion(req.getDireccion())
                .abierto(req.isAbierto())
                .build();
        c = comercioRepository.save(c);
        return new ComercioResponse(c.getId(), c.getNombre(), c.getCategoria(), c.getDireccion(), c.isAbierto());
    }
}
