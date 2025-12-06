-- Criação das tabelas
-- Tabela mesa
-- Tabela reserva
-- Inserts das mesas (8 mesas iniciais)

-- Definição do esquema do banco de dados para o Miniprojeto Web MVC
-- Arquivo: banco/reservas.sql

-- 1. Recria o banco de dados para garantir um ambiente limpo
DROP DATABASE IF EXISTS restaurante_mvc;
CREATE DATABASE restaurante_mvc CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE restaurante_mvc;

-- 2. Criação da tabela de MESAS
-- A coluna 'status' facilita o controle visual no Front-end (Vue.js)
CREATE TABLE mesas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_mesa INT NOT NULL UNIQUE,
    qtd_lugares INT NOT NULL DEFAULT 4,
    status ENUM('LIVRE', 'RESERVADA') NOT NULL DEFAULT 'LIVRE'
) ENGINE=InnoDB;

-- 3. Criação da tabela de RESERVAS
-- Registra quem reservou e quando.
CREATE TABLE reservas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mesa_id INT NOT NULL,
    nome_cliente VARCHAR(100) NOT NULL,
    data_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mesa_reserva FOREIGN KEY (mesa_id) REFERENCES mesas(id)
) ENGINE=InnoDB;

-- =================================================================
-- POPULAÇÃO DE DADOS (Obrigatório conforme critério 9.2 e 3.g)
-- =================================================================

-- Inserindo as Mesas (Simulando um restaurante com 8 mesas)
INSERT INTO mesas (numero_mesa, qtd_lugares, status) VALUES 
(1, 2, 'LIVRE'),
(2, 4, 'LIVRE'),
(3, 4, 'RESERVADA'), -- Já começa ocupada para teste
(4, 6, 'LIVRE'),
(5, 2, 'RESERVADA'), -- Já começa ocupada para teste
(6, 4, 'LIVRE'),
(7, 8, 'LIVRE'),
(8, 4, 'LIVRE');

-- Inserindo Reservas correspondentes às mesas marcadas como 'RESERVADA'
-- Isso garante consistência para a visualização do Funcionário
INSERT INTO reservas (mesa_id, nome_cliente, data_reserva) VALUES 
(3, 'Carlos Silva', NOW()),
(5, 'Ana Maria Braga', NOW());

-- Comentário para o Grupo:
-- Para a lógica do Java MVC:
-- Quando o Cliente reservar uma mesa:
-- 1. Faça um INSERT na tabela 'reservas'.
-- 2. Faça um UPDATE na tabela 'mesas' mudando o status para 'RESERVADA'.
--
-- Para a lógica do AJAX/Vue.js:
-- O endpoint de listagem deve retornar os dados da tabela 'mesas'.
-- Assim, o front-end apenas verifica se status == 'LIVRE' ou 'RESERVADA'.
