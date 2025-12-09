package com.restaurante.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável pela conexão com o banco de dados.
 * Implementa o padrão Singleton para garantir uma única conexão.
 */
public class ConexaoDB {

    // Configurações de conexão com MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/reserva_mesas";
    private static final String USUARIO = "root";
    private static final String SENHA = "1234";

    private static Connection conexao = null;

    // Construtor privado (Singleton)
    private ConexaoDB() {
    }

    /**
     * Retorna uma conexão com o banco de dados.
     * Se a conexão não existir ou estiver fechada, cria uma nova.
     * 
     * @return Connection - objeto de conexão JDBC
     * @throws SQLException se houver erro na conexão
     */
    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            try {
                // Carrega o driver do MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver do MySQL não encontrado: " + e.getMessage());
            }
        }
        return conexao;
    }

    /**
     * Fecha a conexão com o banco de dados.
     */
    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
                conexao = null;
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
