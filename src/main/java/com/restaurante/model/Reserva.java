package com.restaurante.model;

import java.sql.Timestamp;

/**
 * Modelo que representa uma Reserva do restaurante.
 * Corresponde à tabela 'reservas' no banco de dados.
 */
public class Reserva {

    private int id;
    private int mesaId;
    private String nome;
    private Timestamp data;

    // Construtor padrão
    public Reserva() {
    }

    // Construtor completo
    public Reserva(int id, int mesaId, String nome, Timestamp data) {
        this.id = id;
        this.mesaId = mesaId;
        this.nome = nome;
        this.data = data;
    }

    // Construtor sem ID (para inserção)
    public Reserva(int mesaId, String nome) {
        this.mesaId = mesaId;
        this.nome = nome;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMesaId() {
        return mesaId;
    }

    public void setMesaId(int mesaId) {
        this.mesaId = mesaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", mesaId=" + mesaId +
                ", nome='" + nome + '\'' +
                ", data=" + data +
                '}';
    }
}
