package com.restaurante.model;

public class Mesa {
    private int id;
    private int numero;
    private int capacidade;
    private String status;

    // construtor
    public Mesa(int id, int numero, int capacidade, String status) {
        this.id = id;
        this.numero = numero;
        this.capacidade = capacidade;
        this.status = status;
    }
}