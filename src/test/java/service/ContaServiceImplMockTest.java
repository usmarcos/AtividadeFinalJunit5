package service;

import model.Conta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ContaServiceImplMockTest {

    @InjectMocks
    ContaService service;
    @InjectMocks
    ContaServiceImpl serviceImpl;
    @Mock
    private Conta contaMock;

    @BeforeEach
    public void setup() {
        contaMock = mock(Conta.class);
        service = mock(ContaService.class);
        serviceImpl = mock(ContaServiceImpl.class);
    }

    @Test
    public void deveCriarNovaContaCorretamente() throws Exception {
        // Given
        String cpf = "11133355560", nome = "Marcos";
        when(service.criarConta(cpf, nome)).thenReturn(
                new Conta(123, 1, 0.0, nome, cpf, LocalDate.now()));

        // When
        Conta contaCriada = service.criarConta(cpf, nome);

        // Then
        assertEquals(nome, contaCriada.getNomeCliente());
        assertEquals(cpf, contaCriada.getCpf());
        assertEquals(123, contaCriada.getAgencia());
        assertEquals(1, contaCriada.getNumeroConta());
        assertEquals(0.0, contaCriada.getSaldo());
    }

    @Test
    public void deveCriarContaComDataAtual() throws Exception {
        LocalDate data = LocalDate.now();
        // Given
        String cpf = "11133355560", nome = "Marcos";
        when(service.criarConta(cpf, nome)).thenReturn(
                new Conta(123, 1, 0.0, nome, cpf, LocalDate.now()));
        // When
        contaMock = service.criarConta(cpf, nome);
        //Then
        assertEquals(contaMock.getDataCriacaoConta(), data);
    }

    @Test
    public void deveCriarNovaContaSemValor() throws Exception {
        //Given
        String cpf = "11133355560", nome = "Marcos";
        when(service.criarConta(cpf, nome)).thenReturn(contaMock);
        // When
        contaMock = service.criarConta(cpf, nome);
        // Then
        assertEquals(contaMock.getSaldo(), 0.0);
    }

    @Test
    public void deveRealizarDeposito() throws Exception {
        //Given
        double valor = 100.0;
        when(contaMock.getSaldo()).thenReturn(valor);
        //When
        serviceImpl.depositar(contaMock, valor);
        //Then
        assertEquals(contaMock.getSaldo(), valor);
    }
}

