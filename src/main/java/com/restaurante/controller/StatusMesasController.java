package com.restaurante.controller;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/status-mesas")
public class StatusMesasController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        // JSON mockado
        String json = "[{\"mesa\": 1, \"ocupada\": false}, {\"mesa\": 2, \"ocupada\": true}]";

        res.getWriter().write(json);
    }
}
