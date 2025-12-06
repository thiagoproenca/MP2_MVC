INSERT INTO mesas (numero_mesa, qtd_lugares, status) VALUES 
(1, 2, 'LIVRE'),
(2, 4, 'LIVRE'),
(3, 4, 'RESERVADA'),
(4, 6, 'LIVRE'),
(5, 2, 'RESERVADA'),
(6, 4, 'LIVRE'),
(7, 8, 'LIVRE'),
(8, 4, 'LIVRE');

INSERT INTO reservas (mesa_id, nome_cliente, data_reserva) VALUES 
(3, 'Carlos Silva', NOW()),
(5, 'Ana Maria Braga', NOW());