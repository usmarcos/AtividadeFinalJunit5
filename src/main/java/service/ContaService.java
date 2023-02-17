package service;

import model.Conta;

public interface ContaService {
    Conta criarConta(String cpf, String nome) throws Exception;
    void depositar(Conta conta, Double valor) throws Exception;
    void sacar (Conta conta, Double valor) throws Exception;
    Double saldo(Conta conta);
    void transferir(Conta contaOrigem, Conta destino, Double valor) throws Exception;
}
