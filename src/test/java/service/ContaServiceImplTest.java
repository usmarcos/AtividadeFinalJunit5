package service;

import model.Conta;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ContaServiceImplTest {
    private  ContaServiceImpl service = new ContaServiceImpl();
    private Conta conta;
    private Conta contaAuxilio;

    @Test
    public void deveCriarNovaContaCorretamente() throws Exception {
        //Given
        String cpf = "11133355560", nome = "Marcos";
        //When
        conta = service.criarConta(cpf, nome);
        //Then
        assertEquals(conta.getNomeCliente(), nome);
        assertEquals(conta.getCpf(), cpf);
        assertEquals(conta.getAgencia(), service.agencia);
        assertEquals(conta.getNumeroConta(), service.numeroConta);

        Conta contaExpected = conta;
        assertSame(conta, contaExpected);
    }

    @Test
    //evitar alterações graves na conta, como alterar sua criação (pode ser usado para futuramente gerar mais créditos para clientes mais antigos)
    public void deveCriarContaComDataAtual() throws Exception {
        //Given
        String cpf = "11133355560", nome = "Marcos";
        //When
        conta = service.criarConta(cpf, nome);
        //Then
        assertNotEquals(conta.getDataCriacaoConta(), LocalDate.now().plusMonths(1));
        assertEquals(conta.getDataCriacaoConta(), LocalDate.now());
    }

    @Test
    public void deveValidarContaCpfInvalido() throws Exception {
        //Given
        String cpf = "99988877766", nome = "Marcos";
        //When
        Throwable throwable = assertThrows(Exception.class, () -> service.criarConta(cpf, nome));
        //Then
        assertEquals(throwable.getMessage(), "CPF Inválido");
    }

    @Test
    public void deveCriarNovaContaSemValor() throws Exception {
        //Given
        String cpf = "11133355560", nome = "Marcos";
        //When
        conta = service.criarConta(cpf, nome);
        //Then
        assertEquals(conta.getSaldo(), 0.0);
    }

    @Test
    public void deveRealizarDeposito() throws Exception {
        //Given
        String cpf = "11133355560", nome = "Marcos";
        Double valor = 100.0;
        conta = service.criarConta(cpf, nome);
        //When
        service.depositar(conta, valor);
        //Then
        assertEquals(conta.getSaldo(), valor);
    }

    @Test
    public void deveRealizarDepositoMaisValores() throws Exception {
        //Given
        String cpf = "11133355560", nome = "Marcos";
        Double valor = 100.0, valorMais = 50.0;
        conta = service.criarConta(cpf, nome);
        //When
        service.depositar(conta, valor);
        service.depositar(conta, valorMais);
        //Then
        assertEquals(conta.getSaldo(), valor + valorMais);
    }

    @Test
    public void naoDeveDepositarValoresInvalidos() throws Exception {
        //Given
        String cpf = "11133355560", nome = "Marcos";
        Double valorNegativo = -100.0, valorZero = 0.0, valorInvalido = Double.NaN;
        conta = service.criarConta(cpf, nome);
        //When //Then
        assertAll(() -> assertEquals("Informe um valor válido para depósito.", assertThrows(Exception.class, () -> service.depositar(conta, valorNegativo)).getMessage()), () -> assertEquals("Informe um valor válido para depósito.", assertThrows(Exception.class, () -> service.depositar(conta, valorZero)).getMessage()), () -> assertEquals("Informe um valor válido para depósito.", assertThrows(Exception.class, () -> service.depositar(conta, valorInvalido)).getMessage()));
    }

    @Test
    public void deveRealizarSaque() throws Exception {
        //Given
        String cpf = "11133355560", nome = "Marcos";
        Double valor = 100.0, saque = 50.0;
        conta = service.criarConta(cpf, nome);
        service.depositar(conta, valor);
        //When
        service.sacar(conta, saque);
        //Then
        assertEquals(conta.getSaldo(), valor - saque);
    }

    @Test
    public void naoDeveRealizarSaqueSemSaldo() throws Exception {
        //Given
        String cpf = "11133355560", nome = "Marcos";
        Double saque = 50.0;
        conta = service.criarConta(cpf, nome);
        //When
        //Then
        assertEquals("Saldo insuficiente ou valor inválido", assertThrows(Exception.class, () -> service.sacar(conta, saque)).getMessage());
    }

    @Test
    public void naoDeveRealizarSaqueValoresInvalidos() throws Exception {
        //Given
        String cpf = "11133355560", cpfDois = "11900079674", nome = "Marcos", nomeDois = "Teste";
        Double saqueNegativo = -1.0, saqueInvalido = Double.NaN;
        conta = service.criarConta(cpf, nome);
        contaAuxilio = service.criarConta(cpfDois, nomeDois);
        //When
        String throwableNegativo = assertThrows(Exception.class, () -> service.sacar(conta, saqueNegativo)).getMessage();
        String throwableInvalido = assertThrows(Exception.class, () -> service.sacar(contaAuxilio, saqueInvalido)).getMessage();
        //Then
        assertEquals("Saldo insuficiente ou valor inválido", throwableNegativo);
        assertEquals("Saldo insuficiente ou valor inválido", throwableInvalido);
    }

    @Test
    public void deveEmitirSaldoCorretamente() throws Exception {
        //Given
        String cpf = "11133355560", nome = "Marcos";
        Double valor = 100.0;
        conta = service.criarConta(cpf, nome);
        service.depositar(conta, valor);
        //When //Then
        assertEquals(conta.getSaldo(), service.saldo(conta));
    }

    @Test
    public void deveTransferir() throws Exception {
        //Given
        String cpf = "11133355560", cpfDois = "11900079674", nome = "Origem", nomeDois = "Destino";
        Double valor = 100.0;
        conta = service.criarConta(cpf, nome);
        contaAuxilio = service.criarConta(cpfDois, nomeDois);
        service.depositar(conta, valor);
        //When
        service.transferir(conta, contaAuxilio, valor);
        //Then
        assertEquals(conta.getSaldo(), 0.0);
        assertEquals(contaAuxilio.getSaldo(), valor);
    }

    @Test
    public void naoDeveTransferirValoresInvalidos() throws Exception {
        //Given
        String cpf = "11133355560", cpfDois = "11900079674", nome = "Origem", nomeDois = "Destino";
        conta = service.criarConta(cpf, nome);
        contaAuxilio = service.criarConta(cpfDois, nomeDois);
        //When
        Throwable throwable = assertThrows(Exception.class, () -> service.transferir(conta, contaAuxilio, Double.NaN));
        //Then
        assertEquals("Saldo insuficiente ou valor de transferência inválida.", throwable.getMessage());
    }

    @Test
    public void naoDeveTransferirContaSemSaldoSuficiente() throws Exception {
        //Given
        String cpf = "11133355560", cpfDois = "11900079674", nome = "Origem", nomeDois = "Destino";
        Double valor = 100.0, valorInvalido = 150.0;
        conta = service.criarConta(cpf, nome);
        contaAuxilio = service.criarConta(cpfDois, nomeDois);
        service.depositar(conta, valor);
        //When
        String throwable = assertThrows(Exception.class, () -> service.transferir(conta, contaAuxilio, valorInvalido)).getMessage();
        //Then
        assertEquals("Saldo insuficiente ou valor de transferência inválida.", throwable);
    }
}
