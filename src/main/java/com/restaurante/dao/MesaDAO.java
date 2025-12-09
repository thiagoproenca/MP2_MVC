package com.restaurante.dao;

import com.restaurante.model.Mesa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para operações com a tabela 'mesas'.
 */
public class MesaDAO {

    /**
     * Lista todas as mesas do restaurante.
     * 
     * @return Lista de objetos Mesa
     * @throws SQLException se houver erro na consulta
     */
    public List<Mesa> listarMesas() throws SQLException {
        List<Mesa> mesas = new ArrayList<>();
        String sql = "SELECT id, numero_mesa, qtd_lugares, status FROM mesas";

        try (Connection conn = ConexaoDB.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Mesa mesa = new Mesa();
                mesa.setId(rs.getInt("id"));
                mesa.setNumero(rs.getInt("numero_mesa"));
                mesa.setCapacidade(rs.getInt("qtd_lugares"));
                mesa.setStatus(rs.getString("status"));
                mesas.add(mesa);
            }
        }
        return mesas;
    }

    /**
     * Busca uma mesa pelo seu ID.
     * 
     * @param id ID da mesa
     * @return Objeto Mesa ou null se não encontrar
     * @throws SQLException se houver erro na consulta
     */
    public Mesa buscarMesaPorId(int id) throws SQLException {
        String sql = "SELECT id, numero_mesa, qtd_lugares, status FROM mesas WHERE id = ?";

        try (Connection conn = ConexaoDB.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Mesa mesa = new Mesa();
                    mesa.setId(rs.getInt("id"));
                    mesa.setNumero(rs.getInt("numero_mesa"));
                    mesa.setCapacidade(rs.getInt("qtd_lugares"));
                    mesa.setStatus(rs.getString("status"));
                    return mesa;
                }
            }
        }
        return null;
    }

    /**
     * Atualiza o status de uma mesa (LIVRE ou RESERVADA).
     * 
     * @param idMesa ID da mesa a ser atualizada
     * @param status Novo status (LIVRE ou RESERVADA)
     * @throws SQLException se houver erro na atualização
     */
    public void atualizarStatusMesa(int idMesa, String status) throws SQLException {
        String sql = "UPDATE mesas SET status = ? WHERE id = ?";

        try (Connection conn = ConexaoDB.getConexao();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, idMesa);
            stmt.executeUpdate();
        }
    }
}
