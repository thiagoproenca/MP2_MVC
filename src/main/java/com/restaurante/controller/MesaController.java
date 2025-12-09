package com.restaurante.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.restaurante.dao.MesaDAO;
import com.restaurante.model.Mesa;

@WebServlet("/api/mesas")
public class MesaController extends HttpServlet {
	private MesaDAO dao = new MesaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

		Gson gson = new Gson();

		String s_id = req.getParameter("id");

		// checar se houve uma busca por uma mesa específica
		if(s_id != null) {
			int id = Integer.parseInt(s_id);

			// tenta encontrar a mesa pelo id passado
			try {
				Mesa mesa = dao.buscarMesaPorId(id);
				res.getWriter().write( gson.toJson(mesa) );
				return;
			} catch(SQLException e) {
				System.out.println("Erro ao procurar pela mesa: " + e.getMessage());
			}
		}

		// listar todas as mesas cadastradas no banco
		List<Mesa> mesas = new ArrayList<>();

		try {
			mesas = dao.listarMesas();
		} catch (SQLException e) {
			System.out.println("Não foi possível listar as mesas disponíveis: " + e.getMessage());
		}

		// retorna o arquivo JSON com todas as mesas recuperadas
        res.getWriter().write( gson.toJson(mesas) );
    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

		// atualiza o status da mesa pelo id
		int id = Integer.parseInt(req.getParameter("idMesa"));
		String status = req.getParameter("status");

		try {
			dao.atualizarStatusMesa(id, status);
		} catch(SQLException e) {
			System.out.println("Não foi possível atualizar o status da mesa: " + e.getMessage());
		}
		
		// retorna que houve sucesso
		res.getWriter().write("{\"success\": " + true + "}");
	}
}
