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
        String json = "[" +
                "{\"mesa\": 1, \"lugares\": 4, \"ocupada\": false}," +
                "{\"mesa\": 2, \"lugares\": 2, \"ocupada\": true}," +
                "{\"mesa\": 3, \"lugares\": 6, \"ocupada\": false}," +
                "{\"mesa\": 4, \"lugares\": 4, \"ocupada\": true}," +
                "{\"mesa\": 5, \"lugares\": 8, \"ocupada\": false}," +
                "{\"mesa\": 6, \"lugares\": 2, \"ocupada\": false}," +
                "{\"mesa\": 7, \"lugares\": 4, \"ocupada\": true}," +
                "{\"mesa\": 8, \"lugares\": 6, \"ocupada\": false}," +
                "{\"mesa\": 9, \"lugares\": 2, \"ocupada\": true}," +
                "{\"mesa\": 10, \"lugares\": 4, \"ocupada\": false}" +
                "]";

        res.getWriter().write(json);
    }
}
