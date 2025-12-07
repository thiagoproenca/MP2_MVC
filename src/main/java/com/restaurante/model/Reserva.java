package com.restaurante.model;

import java.sql.Timestamp;

/**
 * Modelo que representa uma Reserva do restaurante.
 * Corresponde à tabela 'reservas' no banco de dados.
 */
public class Reserva {

    private int id;
    private int mesaId;
    private String nomeCliente;
    private Timestamp dataReserva;

    // Construtor padrão
    public Reserva() {
    }

    // Construtor completo
    public Reserva(int id, int mesaId, String nomeCliente, Timestamp dataReserva) {
        this.id = id;
        this.mesaId = mesaId;
        this.nomeCliente = nomeCliente;
        this.dataReserva = dataReserva;
    }

    // Construtor sem ID (para inserção)
    public Reserva(int mesaId, String nomeCliente) {
        this.mesaId = mesaId;
        this.nomeCliente = nomeCliente;
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

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Timestamp getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(Timestamp dataReserva) {
        this.dataReserva = dataReserva;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", mesaId=" + mesaId +
                ", nomeCliente='" + nomeCliente + '\'' +
                ", dataReserva=" + dataReserva +
                '}';
    }
}
