package com.everfolgar.fastorder.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.everfolgar.fastorder.dto.DetallePedidoResponse;
import com.everfolgar.fastorder.dto.PedidoRequest;
import com.everfolgar.fastorder.dto.PedidoResponse;
import com.everfolgar.fastorder.exception.InsufficientStockException;
import com.everfolgar.fastorder.exception.InvalidStatusException;
import com.everfolgar.fastorder.exception.ResourceNotFoundException;
import com.everfolgar.fastorder.model.DetallePedido;
import com.everfolgar.fastorder.model.EstadoPedido;
import com.everfolgar.fastorder.model.Pedido;
import com.everfolgar.fastorder.model.Producto;
import com.everfolgar.fastorder.model.Usuario;
import com.everfolgar.fastorder.repository.PedidoRepository;
import com.everfolgar.fastorder.repository.ProductoRepository;
import com.everfolgar.fastorder.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private static final BigDecimal COSTO_ENVIO = new BigDecimal("20.00");

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    private String emailActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Transactional
    public PedidoResponse crearPedido(PedidoRequest req) {
        Usuario cliente = usuarioRepository.findByEmail(emailActual())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .fechaPedido(LocalDateTime.now())
                .costoEnvio(COSTO_ENVIO)
                .estado(EstadoPedido.PENDIENTE)
                .build();

        BigDecimal totalProductos = BigDecimal.ZERO;

        for (PedidoRequest.ItemPedidoRequest item : req.getItems()) {
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new InvalidStatusException("Cantidad inválida para producto " + item.getProductoId());
            }
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + item.getProductoId()));

            if (producto.getStock() < item.getCantidad()) {
                throw new InsufficientStockException("Stock insuficiente para " + producto.getNombre()
                        + " (disponible: " + producto.getStock() + ")");
            }

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
            producto.setStock(producto.getStock() - item.getCantidad());
            if (producto.getStock() == 0) {
                producto.setDisponible(false);
            }
            productoRepository.save(producto);

            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subtotal(subtotal)
                    .build();
            pedido.getDetalles().add(detalle);
            totalProductos = totalProductos.add(subtotal);
        }

        pedido.setMontoTotal(totalProductos.add(COSTO_ENVIO));
        pedido = pedidoRepository.save(pedido);
        return toResponse(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> misPedidos() {
        Usuario cliente = usuarioRepository.findByEmail(emailActual())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        return pedidoRepository.findByClienteId(cliente.getId()).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> disponibles() {
        return pedidoRepository.findByEstadoIn(List.of(EstadoPedido.PENDIENTE, EstadoPedido.EN_CAMINO))
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public PedidoResponse actualizarEstado(Long id, EstadoPedido nuevo) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));

        if (nuevo != EstadoPedido.EN_PREPARACION && nuevo != EstadoPedido.EN_CAMINO
                && nuevo != EstadoPedido.ENTREGADO) {
            throw new InvalidStatusException("Estado inválido para esta operación: " + nuevo);
        }

        boolean valido = switch (nuevo) {
            case EN_PREPARACION -> pedido.getEstado() == EstadoPedido.PENDIENTE;
            case EN_CAMINO -> pedido.getEstado() == EstadoPedido.EN_PREPARACION;
            case ENTREGADO -> pedido.getEstado() == EstadoPedido.EN_CAMINO;
            default -> false;
        };
        if (!valido) {
            throw new InvalidStatusException("Transición no permitida: " + pedido.getEstado() + " -> " + nuevo);
        }

        pedido.setEstado(nuevo);
        return toResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponse cancelar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));

        String email = emailActual();
        Usuario actual = usuarioRepository.findByEmail(email).orElseThrow();
        boolean esAdmin = actual.getRol().name().equals("ADMIN");

        if (!esAdmin) {
            if (!pedido.getCliente().getEmail().equals(email)) {
                throw new InvalidStatusException("No puedes cancelar pedidos de otro cliente");
            }
            if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
                throw new InvalidStatusException("El cliente solo puede cancelar pedidos en estado PENDIENTE");
            }
        } else if (pedido.getEstado() == EstadoPedido.ENTREGADO || pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new InvalidStatusException("No se puede cancelar un pedido " + pedido.getEstado());
        }

        for (DetallePedido d : pedido.getDetalles()) {
            Producto p = d.getProducto();
            p.setStock(p.getStock() + d.getCantidad());
            p.setDisponible(true);
            productoRepository.save(p);
        }
        pedido.setEstado(EstadoPedido.CANCELADO);
        return toResponse(pedidoRepository.save(pedido));
    }

    private PedidoResponse toResponse(Pedido p) {
        List<DetallePedidoResponse> detalles = new ArrayList<>();
        for (DetallePedido d : p.getDetalles()) {
            detalles.add(new DetallePedidoResponse(d.getProducto().getId(), d.getProducto().getNombre(),
                    d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal()));
        }
        return new PedidoResponse(p.getId(), p.getCliente().getId(),
                p.getRepartidor() != null ? p.getRepartidor().getId() : null,
                p.getFechaPedido(), p.getCostoEnvio(), p.getMontoTotal(), p.getEstado(), detalles);
    }
}
