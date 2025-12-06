package com.restaurante.controller;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/reservar")
public class ReservaController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        // TODO: Ler parâmetros reais e chamar DAO
        // Por enquanto, resposta simples para integração do front
        String json = "{\"status\": \"ok\", \"mensagem\": \"Reserva recebida\"}";

        res.getWriter().write(json);
    }
}
