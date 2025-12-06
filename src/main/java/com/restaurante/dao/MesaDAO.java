package com.restaurante.dao;

import com.restaurante.model.Mesa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO {
    private static final String LISTAR_MESAS_SQL = "SELECT * FROM mesas";
    // listarMesas()
    // buscarMesaPorId(int id)
    // atualizarStatusMesa(int idMesa, String status)

    public List<Mesa> listarMesas() {
        List<Mesa> mesas = new ArrayList<>();

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(LISTAR_MESAS_SQL);
            ResultSet rs = ps.executeQuery()
            ) {
            
            // passa por todos os resultados recuperados
            while(rs.next()) {
                // recebe todos os parametros
                int id = rs.getInt("id");
                int numero = rs.getInt("numero_mesa");
                int capacidade = rs.getInt("qtd_lugares");
                String status = rs.getString("status");

                // cria o objeto e adiciona ele a lista
                Mesa m = new Mesa(id, numero, capacidade, status);
                mesas.add(m);
            }
        } catch(SQLException exc) {
            System.err.println("Erro ao listar empregados: " + exc.getMessage());
            exc.printStackTrace();
        }

        return mesas;
    }
}
