package util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class ValidaCpfTest {
    @Test
    public void deveValidarCpfValido() {
        //Given
        String cpf = "11133355560";
        //When //Then
        assertTrue(ValidaCpf.validaCpf(cpf));
    }

    @Test
    public void deveValidarCpfInvalido() {
        //Given
        String cpf = "11133355599";
        //When //Then
        assertFalse(ValidaCpf.validaCpf(cpf));
    }
}
