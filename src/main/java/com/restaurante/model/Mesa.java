package com.restaurante.model;

/**
 * Modelo que representa uma Mesa do restaurante.
 * Corresponde à tabela 'mesas' no banco de dados.
 */
public class Mesa {

    private int id;
    private int numeroMesa;
    private int qtdLugares;
    private String status; // LIVRE ou RESERVADA

    // Construtor padrão
    public Mesa() {
    }

    // Construtor completo
    public Mesa(int id, int numeroMesa, int qtdLugares, String status) {
        this.id = id;
        this.numeroMesa = numeroMesa;
        this.qtdLugares = qtdLugares;
        this.status = status;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public int getQtdLugares() {
        return qtdLugares;
    }

    public void setQtdLugares(int qtdLugares) {
        this.qtdLugares = qtdLugares;
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
                ", numeroMesa=" + numeroMesa +
                ", qtdLugares=" + qtdLugares +
                ", status='" + status + '\'' +
                '}';
    }
}
