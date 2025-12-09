package com.restaurante.controller;

import com.google.gson.Gson;
import com.restaurante.dao.MesaDAO;
import com.restaurante.dao.ReservaDAO;
import com.restaurante.model.Reserva;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/api/reservar")
public class ReservaController extends HttpServlet {

	private ReservaDAO reservaDAO = new ReservaDAO();
	private MesaDAO mesaDAO = new MesaDAO();
	private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

		try {
			// ler json do front
			Reserva reserva = gson.fromJson(req.getReader(), Reserva.class);

			if (reserva == null || reserva.getMesaId() <= 0 || reserva.getNome() == null) {
				res.getWriter().write("{\"sucesso\": false, \"mensagem\": \"Dados inválidos enviados.\"}");
				return;
			}

			int idMesa = reserva.getMesaId();

			// mesa reservada
			if (mesaDAO.buscarMesaPorId(idMesa).getStatus().equalsIgnoreCase("RESERVADA")) {
				res.getWriter().write("{\"sucesso\": false, \"mensagem\": \"Mesa já está reservada.\"}");
				return;
			}

			// reservar
			reservaDAO.criarReserva(reserva);

			// mensagem de sucesso
			String jsonResposta = "{\"sucesso\": true, \"idReserva\": " + reserva.getId() + "}";
			res.getWriter().write(jsonResposta);

		} catch (SQLException e) {
			e.printStackTrace();
			res.getWriter().write("{\"sucesso\": false, \"mensagem\": \"Erro interno no servidor.\"}");
		}
	}
}
