package com.unicesumar.doacoes.model;

import java.time.LocalDate;

public class Doacao {

    public static final String STATUS_RECEBIDA = "RECEBIDA";
    public static final String STATUS_DISTRIBUIDA = "DISTRIBUIDA";
    public static final String STATUS_DESCARTADA = "DESCARTADA";

    private String id;
    private String doador;
    private String item;
    private String categoria;
    private int quantidade;
    private String unidade;
    private LocalDate dataRecebimento;
    private LocalDate validade;
    private String status;

    public Doacao() {
    }

    public Doacao(String doador, String item, String categoria, int quantidade,
                  String unidade, LocalDate dataRecebimento, LocalDate validade) {
        this.doador = doador;
        this.item = item;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.dataRecebimento = dataRecebimento;
        this.validade = validade;
        this.status = STATUS_RECEBIDA;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoador() {
        return doador;
    }

    public void setDoador(String doador) {
        this.doador = doador;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public LocalDate getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Doacao{" +
                "id='" + id + '\'' +
                ", doador='" + doador + '\'' +
                ", item='" + item + '\'' +
                ", categoria='" + categoria + '\'' +
                ", quantidade=" + quantidade +
                ", unidade='" + unidade + '\'' +
                ", dataRecebimento=" + dataRecebimento +
                ", validade=" + validade +
                ", status='" + status + '\'' +
                '}';
    }
}
