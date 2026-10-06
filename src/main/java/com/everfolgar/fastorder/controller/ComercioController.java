package com.everfolgar.fastorder.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.everfolgar.fastorder.dto.ComercioRequest;
import com.everfolgar.fastorder.dto.ComercioResponse;
import com.everfolgar.fastorder.dto.ProductoRequest;
import com.everfolgar.fastorder.dto.ProductoResponse;
import com.everfolgar.fastorder.model.CategoriaComercio;
import com.everfolgar.fastorder.service.ComercioService;
import com.everfolgar.fastorder.service.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comercios")
@RequiredArgsConstructor
public class ComercioController {

    private final ComercioService comercioService;
    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ComercioResponse>> listar(@RequestParam(required = false) CategoriaComercio categoria) {
        return ResponseEntity.ok(comercioService.listar(categoria));
    }

    @PostMapping
    public ResponseEntity<ComercioResponse> crear(@Valid @RequestBody ComercioRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comercioService.crear(req));
    }

    @GetMapping("/{id}/productos")
    public ResponseEntity<List<ProductoResponse>> productos(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.listarPorComercio(id));
    }

    @PostMapping("/{id}/productos")
    public ResponseEntity<ProductoResponse> agregarProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(id, req));
    }
}
