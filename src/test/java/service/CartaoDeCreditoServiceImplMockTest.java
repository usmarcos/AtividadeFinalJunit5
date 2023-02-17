package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import model.CartaoDeCredito;
import model.Conta;
import org.mockito.Mock;

import java.time.LocalDate;

public class CartaoDeCreditoServiceImplMockTest {

    @InjectMocks
    private CartaoDeCreditoService service;
    @Mock
    private Conta contaMock;
    @Mock
    private CartaoDeCredito cartaoDeCredito;

    @BeforeEach
    public void setup() {
        contaMock = mock(Conta.class);
        cartaoDeCredito = mock(CartaoDeCredito.class);
        service = mock(CartaoDeCreditoService.class);
    }

    @Test
    public void deveSolicitarNovoCartao() throws Exception {
        // Given
        when(service.solicitarCartao(contaMock)).thenReturn(cartaoDeCredito);
        // When
        CartaoDeCredito resultado = service.solicitarCartao(contaMock);
        // Then
        assertNotNull(resultado);
        assertEquals(cartaoDeCredito, resultado);
    }

    @Test
    public void deveRetornarLimiteDisponivel() {
        // Given
        when(service.limiteDisponivel(cartaoDeCredito)).thenReturn(1000.0);
        // When
        Double resultado = service.limiteDisponivel(cartaoDeCredito);
        // Then
        assertNotNull(resultado);
        assertEquals(1000.0, resultado);
    }

    @Test
    public void deveRealizarNovaCompra() throws Exception {
        // Given
        Double valor = 500.0;
        // When
        service.novaCompra(cartaoDeCredito, valor);
        // Then
        verify(service).novaCompra(cartaoDeCredito, valor);
    }

    @Test
    public void devePagarFatura() throws Exception {
        // Given
        when(cartaoDeCredito.getFatura()).thenReturn(500.0);
        when(cartaoDeCredito.getLimitePadrao()).thenReturn(1500.0);
        when(cartaoDeCredito.getVencimento()).thenReturn(LocalDate.now());

        // When
        CartaoDeCreditoService service = new CartaoDeCreditoServiceImpl();
        boolean result = service.pagarFatura(cartaoDeCredito, 600.0);

        // Then
        verify(cartaoDeCredito).setFatura(0.0);
        verify(cartaoDeCredito).setLimite(1600.0);
        verify(cartaoDeCredito).setVencimento(LocalDate.now().plusMonths(1));
        assertTrue(result);
    }
}