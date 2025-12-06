package com.restaurante.controller;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/mesas")
public class MesaController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        // JSON mockado (substituir por DAO depois)
        String json = "[{\"id\": 1, \"lugares\": 4}, {\"id\": 2, \"lugares\": 2}]";

        res.getWriter().write(json);
    }
}
