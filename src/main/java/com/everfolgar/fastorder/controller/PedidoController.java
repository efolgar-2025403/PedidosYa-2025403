package com.everfolgar.fastorder.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.everfolgar.fastorder.dto.EstadoPedidoRequest;
import com.everfolgar.fastorder.dto.PedidoRequest;
import com.everfolgar.fastorder.dto.PedidoResponse;
import com.everfolgar.fastorder.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedido(req));
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResponse>> misPedidos() {
        return ResponseEntity.ok(pedidoService.misPedidos());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<PedidoResponse>> disponibles() {
        return ResponseEntity.ok(pedidoService.disponibles());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> actualizarEstado(@PathVariable Long id, @Valid @RequestBody EstadoPedidoRequest req) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, req.getEstado()));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelar(id));
    }
}
