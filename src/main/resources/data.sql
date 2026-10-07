INSERT INTO usuarios (id, nombre, direccion, telefono, email, password, rol) VALUES
(1, 'Administrador', 'Zona 1', '55550000', 'admin@fastorder.com', '$2a$10$voRVIlA4XKRZwkyeg0/uieRzZ73vcmRlLGuX3LoZBVYxBoe3Kr37K', 'ADMIN'),
(2, 'Repartidor Uno', 'Zona 2', '55550001', 'repartidor@fastorder.com', '$2a$10$YZOdipYHRm63ZkfXgBj8E.TixxSTSr8i9.7OVwcrSsWyC9hzmjJi6', 'REPARTIDOR'),
(3, 'Cliente Demo', 'Zona 3', '55550002', 'cliente@fastorder.com', '$2a$10$LiZA9wMgccRlqP44b045LOQS7EKx4mVNX.L36sE9ezjlZJVWK3lYG', 'CLIENTE')
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
