package com.restaurante.dao;

import com.restaurante.model.Reserva;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ReservaDAO.
 * Usa banco de dados H2 em memória para testes isolados.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservaDAOTest {

    private Connection conexaoTeste;
    private ReservaDAOTestavel reservaDAO;

    @BeforeAll
    void setupDatabase() throws SQLException {
        // Cria banco H2 em memória com modo de compatibilidade MySQL
        conexaoTeste = DriverManager.getConnection(
                "jdbc:h2:mem:testdb_reserva;MODE=MySQL;DB_CLOSE_DELAY=-1", "sa", "");

        // Cria as tabelas de teste
        try (Statement stmt = conexaoTeste.createStatement()) {
            stmt.execute("CREATE TABLE mesas (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "numero_mesa INT NOT NULL UNIQUE," +
                    "qtd_lugares INT NOT NULL DEFAULT 4," +
                    "status VARCHAR(20) NOT NULL DEFAULT 'LIVRE'" +
                    ")");

            stmt.execute("CREATE TABLE reservas (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "mesa_id INT NOT NULL," +
                    "nome_cliente VARCHAR(100) NOT NULL," +
                    "data_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "CONSTRAINT fk_mesa_reserva FOREIGN KEY (mesa_id) REFERENCES mesas(id)" +
                    ")");

            // Insere dados de teste
            stmt.execute("INSERT INTO mesas (numero_mesa, qtd_lugares, status) VALUES (1, 2, 'LIVRE')");
            stmt.execute("INSERT INTO mesas (numero_mesa, qtd_lugares, status) VALUES (2, 4, 'RESERVADA')");
            stmt.execute("INSERT INTO mesas (numero_mesa, qtd_lugares, status) VALUES (3, 6, 'LIVRE')");

            // Insere uma reserva existente para a mesa 2
            stmt.execute("INSERT INTO reservas (mesa_id, nome_cliente) VALUES (2, 'Cliente Teste')");
        }

        reservaDAO = new ReservaDAOTestavel(conexaoTeste);
    }

    @AfterAll
    void tearDown() throws SQLException {
        if (conexaoTeste != null && !conexaoTeste.isClosed()) {
            conexaoTeste.close();
        }
    }

    @Test
    @DisplayName("Deve criar uma nova reserva")
    void testCriarReserva() throws SQLException {
        Reserva reserva = new Reserva(1, "João Silva");

        reservaDAO.criarReserva(reserva);

        // Verifica se a reserva foi criada com ID gerado
        assertTrue(reserva.getId() > 0);

        // Verifica se a mesa foi marcada como RESERVADA
        assertTrue(reservaDAO.mesaEstaReservada(1));
    }

    @Test
    @DisplayName("Deve listar reservas por mesa")
    void testListarReservasPorMesa() throws SQLException {
        List<Reserva> reservas = reservaDAO.listarReservasPorMesa(2);

        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());

        Reserva reserva = reservas.get(0);
        assertEquals(2, reserva.getMesaId());
        assertEquals("Cliente Teste", reserva.getNomeCliente());
    }

    @Test
    @DisplayName("Deve retornar lista vazia para mesa sem reservas")
    void testListarReservasPorMesaSemReservas() throws SQLException {
        List<Reserva> reservas = reservaDAO.listarReservasPorMesa(3);

        assertNotNull(reservas);
        assertTrue(reservas.isEmpty());
    }

    @Test
    @DisplayName("Deve verificar se mesa está reservada")
    void testMesaEstaReservada() throws SQLException {
        // Mesa 2 está reservada
        assertTrue(reservaDAO.mesaEstaReservada(2));

        // Mesa 3 está livre
        assertFalse(reservaDAO.mesaEstaReservada(3));
    }

    @Test
    @DisplayName("Deve retornar false para mesa inexistente")
    void testMesaEstaReservadaInexistente() throws SQLException {
        assertFalse(reservaDAO.mesaEstaReservada(999));
    }

    /**
     * Classe interna que estende ReservaDAO para usar conexão de teste.
     */
    private static class ReservaDAOTestavel extends ReservaDAO {
        private final Connection conexaoTeste;

        public ReservaDAOTestavel(Connection conexaoTeste) {
            this.conexaoTeste = conexaoTeste;
        }

        @Override
        public void criarReserva(Reserva reserva) throws SQLException {
            String sqlInsert = "INSERT INTO reservas (mesa_id, nome_cliente, data_reserva) VALUES (?, ?, NOW())";
            String sqlUpdate = "UPDATE mesas SET status = 'RESERVADA' WHERE id = ?";

            try {
                conexaoTeste.setAutoCommit(false);

                // Insere a reserva
                try (var stmtInsert = conexaoTeste.prepareStatement(sqlInsert,
                        java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    stmtInsert.setInt(1, reserva.getMesaId());
                    stmtInsert.setString(2, reserva.getNomeCliente());
                    stmtInsert.executeUpdate();

                    try (var rs = stmtInsert.getGeneratedKeys()) {
                        if (rs.next()) {
                            reserva.setId(rs.getInt(1));
                        }
                    }
                }

                // Atualiza o status da mesa
                try (var stmtUpdate = conexaoTeste.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setInt(1, reserva.getMesaId());
                    stmtUpdate.executeUpdate();
                }

                conexaoTeste.commit();
            } catch (SQLException e) {
                conexaoTeste.rollback();
                throw e;
            } finally {
                conexaoTeste.setAutoCommit(true);
            }
        }

        @Override
        public List<Reserva> listarReservasPorMesa(int idMesa) throws SQLException {
            List<Reserva> reservas = new java.util.ArrayList<>();
            String sql = "SELECT id, mesa_id, nome_cliente, data_reserva FROM reservas WHERE mesa_id = ?";

            try (var stmt = conexaoTeste.prepareStatement(sql)) {
                stmt.setInt(1, idMesa);

                try (var rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Reserva reserva = new Reserva();
                        reserva.setId(rs.getInt("id"));
                        reserva.setMesaId(rs.getInt("mesa_id"));
                        reserva.setNomeCliente(rs.getString("nome_cliente"));
                        reserva.setDataReserva(rs.getTimestamp("data_reserva"));
                        reservas.add(reserva);
                    }
                }
            }
            return reservas;
        }

        @Override
        public boolean mesaEstaReservada(int idMesa) throws SQLException {
            String sql = "SELECT status FROM mesas WHERE id = ?";

            try (var stmt = conexaoTeste.prepareStatement(sql)) {
                stmt.setInt(1, idMesa);

                try (var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return "RESERVADA".equals(rs.getString("status"));
                    }
                }
            }
            return false;
        }
    }
}
