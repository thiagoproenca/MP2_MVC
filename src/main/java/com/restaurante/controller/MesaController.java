package com.restaurante.controller;

import com.restaurante.dao.MesaDAO;
import com.restaurante.model.Mesa;

import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/mesas")
public class MesaController extends HttpServlet {
	private MesaDAO dao = new MesaDAO();
	private List<Mesa> mesas = new ArrayList<>();
	private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

		// recupera as mesas cadastradas no banco
		try {
			mesas = dao.listarMesas();
		} catch (SQLException e) {
			System.out.println("Não foi possível listar as mesas disponíveis: " + e.getMessage());
		}

		// retorna o arquivo JSON com todas as mesas recuperadas
        res.getWriter().write( gson.toJson(mesas) );
    }
}
