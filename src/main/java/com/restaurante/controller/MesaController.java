package com.restaurante.controller;

import com.restaurante.model.Mesa;
import com.restaurante.dao.MesaDAO;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.List;

@WebServlet("/mesas")
public class MesaController extends HttpServlet {
    // atributos
    private MesaDAO dao = new MesaDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        /*
        // JSON mockado (substituir por DAO depois)
        String json = "[{\"id\": 1, \"lugares\": 4}, {\"id\": 2, \"lugares\": 2}]";

        res.getWriter().write(json);
        */
       

        // recupera lista de mesas - fornecida por MesaDAO
        List<Mesa> lista_mesas = dao.listarMesas();

        // enviar a resposta como JSON
        res.getWriter().write(gson.toJson(lista_mesas));
    }
}
