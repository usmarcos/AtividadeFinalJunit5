package model;

import java.time.LocalDate;

public class Conta {
    private Integer agencia;
    private Integer numeroConta;
    private Double saldo;
    private String nomeCliente;
    private String cpf;
    private LocalDate dataCriacaoConta;

    public Conta(Integer agencia, Integer numeroConta, Double saldo, String nomeCliente, String cpf, LocalDate dataCriacaoConta) {
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.saldo = saldo;
        this.nomeCliente = nomeCliente;
        this.cpf = cpf;
        this.dataCriacaoConta = dataCriacaoConta;
    }

    public Integer getAgencia() {
        return agencia;
    }

    public Integer getNumeroConta() {
        return numeroConta;
    }

    public Double getSaldo() {
        return saldo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public LocalDate getDataCriacaoConta() {
        return dataCriacaoConta;
    }

}
