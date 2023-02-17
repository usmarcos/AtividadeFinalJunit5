package model;

import java.time.LocalDate;
import java.util.Random;
import java.util.stream.Collectors;

public class CartaoDeCredito{
    private Conta conta;
    private String numeroCartao;
    private Double limite;
    private Double fatura;
    private Double limitePadrao = 1500.00;
    private LocalDate vencimento;

    public CartaoDeCredito(Conta conta) {
        this.conta = conta;
        this.numeroCartao = new Random().ints(0, 10).limit(16).mapToObj(String::valueOf).collect(Collectors.joining());;
        this.limite = limitePadrao;
        this.fatura = 0.0;
        this.vencimento = LocalDate.now().plusMonths(1);
    }

    public Conta getConta() {
        return conta;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public Double getLimite() {
        return limite;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public Double getFatura() {
        return fatura;
    }

    public void setFatura(Double fatura) {
        this.fatura = fatura;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public Double getLimitePadrao() {
        return limitePadrao;
    }
}