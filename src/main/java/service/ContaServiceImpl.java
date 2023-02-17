package service;

import model.Conta;
import util.ValidaCpf;

import java.time.LocalDate;

public class ContaServiceImpl implements ContaService {

    Integer agencia = 1234;
    Integer numeroConta = 0000;

    @Override
    public Conta criarConta(String cpf, String nome) throws Exception {
        if (!ValidaCpf.validaCpf(cpf)) throw new Exception("CPF Inválido");
        return new Conta(agencia, ++numeroConta, 0.0, nome, cpf, LocalDate.now());
    }

    @Override
    public void depositar(Conta conta, Double valor) throws Exception {
        if (valor <= 0 || Double.isNaN(valor)) throw new Exception("Informe um valor válido para depósito.");
        conta.setSaldo(conta.getSaldo() + valor);
    }

    @Override
    public void sacar(Conta conta, Double valor) throws Exception {
        if (conta.getSaldo() < valor || Double.isNaN(valor)|| valor <= Double.valueOf(0.0)) throw new Exception("Saldo insuficiente ou valor inválido");
        conta.setSaldo(conta.getSaldo() - valor);
    }

    @Override
    public Double saldo(Conta conta) {
       return conta.getSaldo();
    }

    @Override
    public void transferir(Conta origem, Conta destino, Double valor) throws Exception {
        if (origem.getSaldo() < valor || valor <= 0 || Double.isNaN(valor)) throw new Exception("Saldo insuficiente ou valor de transferência inválida.");
        origem.setSaldo(origem.getSaldo() - valor);
        destino.setSaldo(destino.getSaldo() + valor);
    }
}
