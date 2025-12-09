package com.restaurante.model;

/**
 * Modelo que representa uma Mesa do restaurante.
 * Corresponde à tabela 'mesas' no banco de dados.
 */
public class Mesa {

    private int id;
    private int numero;
    private int capacidade;
    private String status; // LIVRE ou RESERVADA

    // Construtor padrão
    public Mesa() {
    }

    // Construtor completo
    public Mesa(int id, int numero, int capacidade, String status) {
        this.id = id;
        this.numero = numero;
        this.capacidade = capacidade;
        this.status = status;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Mesa{" +
                "id=" + id +
                ", numero=" + numero +
                ", capacidade=" + capacidade +
                ", status='" + status + '\'' +
                '}';
    }
}
