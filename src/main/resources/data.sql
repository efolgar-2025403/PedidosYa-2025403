INSERT INTO usuarios (id, nombre, direccion, telefono, email, password, rol) VALUES
(1, 'Administrador', 'Zona 1', '55550000', 'admin@fastorder.com', '$2a$10$jU1m3Ptc.ECo.bKy49.CY.cGqE/nfyTRaN8R26jJARIIp7lYNQ33C', 'ADMIN'),
(2, 'Repartidor Uno', 'Zona 2', '55550001', 'repartidor@fastorder.com', '$2a$10$jU1m3Ptc.ECo.bKy49.CY.cGqE/nfyTRaN8R26jJARIIp7lYNQ33C', 'REPARTIDOR'),
(3, 'Cliente Demo', 'Zona 3', '55550002', 'cliente@fastorder.com', '$2a$10$jU1m3Ptc.ECo.bKy49.CY.cGqE/nfyTRaN8R26jJARIIp7lYNQ33C', 'CLIENTE')
ON CONFLICT (email) DO NOTHING;

INSERT INTO comercios (id, nombre, categoria, direccion, abierto) VALUES
(1, 'Restaurante El Fogón', 'RESTAURANTE', 'Zona 10, Ciudad', true)
ON CONFLICT DO NOTHING;

INSERT INTO productos (id, comercio_id, nombre, precio, stock, disponible) VALUES
(1, 1, 'Hamburguesa Clásica', 35.00, 50, true),
(2, 1, 'Pizza Personal', 45.00, 30, true),
(3, 1, 'Refresco 12oz', 8.00, 100, true)
ON CONFLICT DO NOTHING;

SELECT setval('usuarios_id_seq', (SELECT COALESCE(MAX(id),1) FROM usuarios));
SELECT setval('comercios_id_seq', (SELECT COALESCE(MAX(id),1) FROM comercios));
SELECT setval('productos_id_seq', (SELECT COALESCE(MAX(id),1) FROM productos));
