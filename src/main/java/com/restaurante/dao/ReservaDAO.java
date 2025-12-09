package com.restaurante.dao;

import com.restaurante.model.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para operações com a tabela 'reservas'.
 */
public class ReservaDAO {

    /**
     * Cria uma nova reserva no banco de dados.
     * Também atualiza o status da mesa para 'RESERVADA'.
     * 
     * @param reserva Objeto Reserva com os dados da reserva
     * @throws SQLException se houver erro na inserção
     */
    public void criarReserva(Reserva reserva) throws SQLException {
        String sqlInsert = "INSERT INTO reservas (mesa_id, nome_cliente, data_reserva) VALUES (?, ?, NOW())";
        String sqlUpdate = "UPDATE mesas SET status = 'RESERVADA' WHERE id = ?";

        Connection conn = ConexaoDB.getConexao();
        try {
            conn.setAutoCommit(false);

            // Insere a reserva
            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                stmtInsert.setInt(1, reserva.getMesaId());
                stmtInsert.setString(2, reserva.getNome());
                stmtInsert.executeUpdate();

                // Obtém o ID gerado
                try (ResultSet rs = stmtInsert.getGeneratedKeys()) {
                    if (rs.next()) {
                        reserva.setId(rs.getInt(1));
                    }
                }
            }

            // Atualiza o status da mesa
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setInt(1, reserva.getMesaId());
                stmtUpdate.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * Lista todas as reservas de uma mesa específica.
     * 
     * @param idMesa ID da mesa
     * @return Lista de reservas da mesa
     * @throws SQLException se houver erro na consulta
     */
    public List<Reserva> listarReservasPorMesa(int idMesa) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT id, mesa_id, nome_cliente, data_reserva FROM reservas WHERE mesa_id = ?";

        try (Connection conn = ConexaoDB.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMesa);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reserva reserva = new Reserva();
                    reserva.setId(rs.getInt("id"));
                    reserva.setMesaId(rs.getInt("mesa_id"));
                    reserva.setNome(rs.getString("nome_cliente"));
                    reserva.setData(rs.getTimestamp("data_reserva"));
                    reservas.add(reserva);
                }
            }
        }
        return reservas;
    }

    /**
     * Verifica se uma mesa está reservada.
     * 
     * @param idMesa ID da mesa
     * @return true se a mesa está reservada, false caso contrário
     * @throws SQLException se houver erro na consulta
     */
    public boolean mesaEstaReservada(int idMesa) throws SQLException {
        String sql = "SELECT status FROM mesas WHERE id = ?";

        try (Connection conn = ConexaoDB.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMesa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return "RESERVADA".equals(rs.getString("status"));
                }
            }
        }
        return false;
    }
}
