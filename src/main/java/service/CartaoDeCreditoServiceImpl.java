package service;

import model.CartaoDeCredito;
import model.Conta;

public class CartaoDeCreditoServiceImpl implements CartaoDeCreditoService {

    @Override
    public CartaoDeCredito solicitarCartao(Conta conta) throws Exception {
        CartaoDeCredito cartao;
        try {
            cartao = new CartaoDeCredito(conta);
        } catch (Exception e) {
            throw new Exception("Conta inexistente, consulte seu gerente para mais informações.");
        }
        return cartao;
    }

    @Override
    public Double limiteDisponivel(CartaoDeCredito cartao) {
        return cartao.getLimite();
    }

    @Override
    public void novaCompra(CartaoDeCredito cartao, Double valor) throws Exception {
        if (cartao.getLimite() < valor || Double.isNaN(valor))
            throw new Exception("Limite indisponível para esta compra. Consulte o credor para solicitar mais crédito. \nOu valor de compra inválido.");
        cartao.setFatura(cartao.getFatura() + valor);
        cartao.setLimite(cartao.getLimite() - valor);
    }

    @Override
    public boolean pagarFatura(CartaoDeCredito cartao, Double valor) throws Exception {
        if (cartao.getFatura() > valor || Double.isNaN(valor)){
            throw new Exception("O valor da fatura é maior que o valor a ser pago. O banco não permite pagamentos minimos.\n Ou valor de pagamento inválido.");
        }
        //caso haja pagamento excedente
        Double limiteAtualizado = (valor - cartao.getFatura()) + cartao.getLimitePadrao();
        cartao.setFatura(0.0);
        cartao.setLimite(limiteAtualizado);
        cartao.setVencimento(cartao.getVencimento().plusMonths(1));
        return true;
    }
}
