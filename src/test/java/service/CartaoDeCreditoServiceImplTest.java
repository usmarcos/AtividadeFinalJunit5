package service;

import model.CartaoDeCredito;
import model.Conta;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class CartaoDeCreditoServiceImplTest {
    private CartaoDeCreditoServiceImpl service = new CartaoDeCreditoServiceImpl();
    private Conta conta;
    private CartaoDeCredito cartaoCredito;
    private String cpf = "11133355560", nome = "Origem";

    @BeforeEach
    public void setup() throws Exception {
        conta = new ContaServiceImpl().criarConta(cpf, nome);
    }

    @Test
    public void deveSolicitarCartaoCorretamente() throws Exception {
        //Given //When
        cartaoCredito = service.solicitarCartao(conta);
        //Then
        assertAll(
                () -> assertEquals(cartaoCredito.getConta().getNomeCliente(), nome),
                () -> assertEquals(cartaoCredito.getConta().getCpf(), cpf),
                () -> assertEquals(cartaoCredito.getConta().getAgencia(), new ContaServiceImpl().agencia),
                () -> assertEquals(cartaoCredito.getConta().getNumeroConta(), ++new ContaServiceImpl().numeroConta),
                () -> assertEquals(cartaoCredito.getNumeroCartao().length(), Integer.valueOf(16)),
                () -> assertEquals(cartaoCredito.getLimite(), cartaoCredito.getLimitePadrao()),
                () -> assertEquals(cartaoCredito.getFatura(), 0.0),
                () -> assertEquals(cartaoCredito.getVencimento(), LocalDate.now().plusMonths(1)));
    }

    @Test
    public void naoDeveSolicitarCartaoSemContaCorrente() throws Exception {
        //Given
        //When //Then
        try {
            service.solicitarCartao(conta);
        } catch (Exception e) {
            assertEquals("Conta inexistente, consulte seu gerente para mais informações.", e.getMessage());
        }
    }

    @Test
    public void validaLimiteDisponivelSemCompraRealizada() throws Exception {
        //Given
        cartaoCredito = service.solicitarCartao(conta);
        //When //Then
        assertEquals(cartaoCredito.getLimite(), service.limiteDisponivel(cartaoCredito));
    }

    @Test
    public void validaLimiteDisponivelAoRealizarCompra() throws Exception {
        //Given
        Double compra = 150.0;
        cartaoCredito = service.solicitarCartao(conta);
        //When
        service.novaCompra(cartaoCredito, compra);
        // Then
        assertEquals(cartaoCredito.getLimite(), cartaoCredito.getLimitePadrao() - compra);
    }

    @Test
    public void validaLimiteDisponivelAoRealizarVariasCompras() throws Exception {
        //Given
        Double compraUm = 150.0, compraDois = 1100.0, compraTres = 250.0;
        cartaoCredito = service.solicitarCartao(conta);
        //When
        service.novaCompra(cartaoCredito, compraUm);
        service.novaCompra(cartaoCredito, compraDois);
        service.novaCompra(cartaoCredito, compraTres);
        // Then
        assertEquals(cartaoCredito.getLimite(), cartaoCredito.getLimitePadrao() - (compraUm + compraDois + compraTres));
    }

    @Test
    public void naoDeveRealizarCompraSemLimite() throws Exception {
        //Given
        Double compraUm = 150.0, compraDois = 1300.0, compraTres = 300.0;
        cartaoCredito = service.solicitarCartao(conta);
        //When
        service.novaCompra(cartaoCredito, compraUm);
        service.novaCompra(cartaoCredito, compraDois);
        Throwable throwable = assertThrows(Exception.class, () -> service.novaCompra(cartaoCredito, compraTres));
        // Then
        assertEquals("Limite indisponível para esta compra. Consulte o credor para solicitar mais crédito. " + "\nOu valor de compra inválido.", throwable.getMessage());
    }

    @Test
    public void naoDeveRealizarCompraComValoresInvalidos() throws Exception {
        //Given
        Double compraUm = 150.0, compraDois = 1300.0, compraTres = Double.NaN;
        cartaoCredito = service.solicitarCartao(conta);
        //When
        service.novaCompra(cartaoCredito, compraUm);
        service.novaCompra(cartaoCredito, compraDois);
        Throwable throwable = assertThrows(Exception.class, () -> service.novaCompra(cartaoCredito, compraTres));
        // Then
        assertEquals("Limite indisponível para esta compra. Consulte o credor para solicitar mais crédito. " + "\nOu valor de compra inválido.", throwable.getMessage());
    }

    @Test
    public void devePagarFatura() throws Exception {
        //Given
        Double compraUm = 150.0, compraDois = 1100.0, compraTres = 250.0;
        cartaoCredito = service.solicitarCartao(conta);
        service.novaCompra(cartaoCredito, compraUm);
        service.novaCompra(cartaoCredito, compraDois);
        service.novaCompra(cartaoCredito, compraTres);
        //When
        boolean pagamento = service.pagarFatura(cartaoCredito, (compraUm + compraDois + compraTres));
        // Then
        assertTrue(pagamento);
        assertEquals(cartaoCredito.getLimite(), cartaoCredito.getLimitePadrao());
    }

    @Test
    public void naoDevePagarFaturaComValorMinimo() throws Exception {
        //Given
        Double compraUm = 150.0, compraDois = 1100.0, compraTres = 250.0, valorAPagar = 1000.00;
        cartaoCredito = service.solicitarCartao(conta);
        service.novaCompra(cartaoCredito, compraUm);
        service.novaCompra(cartaoCredito, compraDois);
        service.novaCompra(cartaoCredito, compraTres);
        //When
        Throwable throwable = assertThrows(Exception.class, () -> service.pagarFatura(cartaoCredito, valorAPagar));
        // Then
        assertEquals("O valor da fatura é maior que o valor a ser pago. O banco não permite pagamentos minimos." + "\n Ou valor de pagamento inválido.", throwable.getMessage());
    }

    @Test
    public void naoDevePagarFaturaComValorInvalido() throws Exception {
        //Given
        Double compraUm = 150.0, valorAPagar = Double.NaN;
        cartaoCredito = service.solicitarCartao(conta);
        service.novaCompra(cartaoCredito, compraUm);
        //When
        Throwable throwable = assertThrows(Exception.class, () -> service.pagarFatura(cartaoCredito, valorAPagar));
        // Then
        assertEquals("O valor da fatura é maior que o valor a ser pago. O banco não permite pagamentos minimos." + "\n Ou valor de pagamento inválido.", throwable.getMessage());
    }

    @Test
    public void deveGerarCreditoExcedenteQuandoValorPagamentoMaiorQueFatura() throws Exception {
        //Given
        Double compraUm = 150.0, compraDois = 1350.0, pagamentoExcedente = 2000.00;
        cartaoCredito = service.solicitarCartao(conta);
        service.novaCompra(cartaoCredito, compraUm);
        service.novaCompra(cartaoCredito, compraDois);
        //When
        boolean pagamento = service.pagarFatura(cartaoCredito, pagamentoExcedente);
        // Then
        assertTrue(pagamento);
        assertEquals(cartaoCredito.getLimite(), cartaoCredito.getLimitePadrao() + (pagamentoExcedente - (compraUm + compraDois)));
        assertEquals(cartaoCredito.getVencimento(), LocalDate.now().plusMonths(2));
    }
}
