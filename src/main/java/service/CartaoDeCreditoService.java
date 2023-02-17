package service;

import model.CartaoDeCredito;
import model.Conta;

public interface CartaoDeCreditoService {
    CartaoDeCredito solicitarCartao(Conta conta) throws Exception;
    Double limiteDisponivel(CartaoDeCredito cartao);
    void novaCompra(CartaoDeCredito cartao, Double valor) throws Exception;
    boolean pagarFatura(CartaoDeCredito cartao, Double valor) throws Exception;
}
