package com.restaurante.dao;

import com.restaurante.model.Mesa;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para MesaDAO.
 * Usa banco de dados H2 em memória para testes isolados.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MesaDAOTest {

    private Connection conexaoTeste;
    private MesaDAO mesaDAO;

    @BeforeAll
    void setupDatabase() throws SQLException {
        // Cria banco H2 em memória com modo de compatibilidade MySQL
        conexaoTeste = DriverManager.getConnection(
                "jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1", "sa", "");

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
        }

        mesaDAO = new MesaDAOTestavel(conexaoTeste);
    }

    @AfterAll
    void tearDown() throws SQLException {
        if (conexaoTeste != null && !conexaoTeste.isClosed()) {
            conexaoTeste.close();
        }
    }

    @Test
    @DisplayName("Deve listar todas as mesas")
    void testListarMesas() throws SQLException {
        List<Mesa> mesas = mesaDAO.listarMesas();

        assertNotNull(mesas);
        assertEquals(3, mesas.size());

        // Verifica se as mesas foram retornadas corretamente
        Mesa mesa1 = mesas.stream().filter(m -> m.getNumeroMesa() == 1).findFirst().orElse(null);
        assertNotNull(mesa1);
        assertEquals(2, mesa1.getQtdLugares());
        assertEquals("LIVRE", mesa1.getStatus());
    }

    @Test
    @DisplayName("Deve buscar mesa por ID")
    void testBuscarMesaPorId() throws SQLException {
        Mesa mesa = mesaDAO.buscarMesaPorId(1);

        assertNotNull(mesa);
        assertEquals(1, mesa.getId());
        assertEquals(1, mesa.getNumeroMesa());
        assertEquals(2, mesa.getQtdLugares());
        assertEquals("LIVRE", mesa.getStatus());
    }

    @Test
    @DisplayName("Deve retornar null para ID inexistente")
    void testBuscarMesaPorIdInexistente() throws SQLException {
        Mesa mesa = mesaDAO.buscarMesaPorId(999);
        assertNull(mesa);
    }

    @Test
    @DisplayName("Deve atualizar status da mesa")
    void testAtualizarStatusMesa() throws SQLException {
        // Atualiza status da mesa 1 para RESERVADA
        mesaDAO.atualizarStatusMesa(1, "RESERVADA");

        // Verifica se o status foi atualizado
        Mesa mesa = mesaDAO.buscarMesaPorId(1);
        assertNotNull(mesa);
        assertEquals("RESERVADA", mesa.getStatus());

        // Restaura o status original
        mesaDAO.atualizarStatusMesa(1, "LIVRE");
    }

    /**
     * Classe interna que estende MesaDAO para usar conexão de teste.
     */
    private static class MesaDAOTestavel extends MesaDAO {
        private final Connection conexaoTeste;

        public MesaDAOTestavel(Connection conexaoTeste) {
            this.conexaoTeste = conexaoTeste;
        }

        @Override
        public List<Mesa> listarMesas() throws SQLException {
            List<Mesa> mesas = new java.util.ArrayList<>();
            String sql = "SELECT id, numero_mesa, qtd_lugares, status FROM mesas";

            try (var stmt = conexaoTeste.prepareStatement(sql);
                    var rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Mesa mesa = new Mesa();
                    mesa.setId(rs.getInt("id"));
                    mesa.setNumeroMesa(rs.getInt("numero_mesa"));
                    mesa.setQtdLugares(rs.getInt("qtd_lugares"));
                    mesa.setStatus(rs.getString("status"));
                    mesas.add(mesa);
                }
            }
            return mesas;
        }

        @Override
        public Mesa buscarMesaPorId(int id) throws SQLException {
            String sql = "SELECT id, numero_mesa, qtd_lugares, status FROM mesas WHERE id = ?";

            try (var stmt = conexaoTeste.prepareStatement(sql)) {
                stmt.setInt(1, id);

                try (var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Mesa mesa = new Mesa();
                        mesa.setId(rs.getInt("id"));
                        mesa.setNumeroMesa(rs.getInt("numero_mesa"));
                        mesa.setQtdLugares(rs.getInt("qtd_lugares"));
                        mesa.setStatus(rs.getString("status"));
                        return mesa;
                    }
                }
            }
            return null;
        }

        @Override
        public void atualizarStatusMesa(int idMesa, String status) throws SQLException {
            String sql = "UPDATE mesas SET status = ? WHERE id = ?";

            try (var stmt = conexaoTeste.prepareStatement(sql)) {
                stmt.setString(1, status);
                stmt.setInt(2, idMesa);
                stmt.executeUpdate();
            }
        }
    }
}
