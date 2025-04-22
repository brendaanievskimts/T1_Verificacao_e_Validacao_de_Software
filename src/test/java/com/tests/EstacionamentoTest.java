package com.tests;

import com.data.Cliente;
import com.data.Estacionamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EstacionamentoTest {
    private Estacionamento estacionamento = new Estacionamento();

    @BeforeEach
    void setUp() {
        estacionamento = new Estacionamento();
    }

    // **Cortesia**
    @Test
    void calcularValorCortesiaComum() {
        LocalDateTime entrada = LocalDateTime.of(2025, 4, 22, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2025, 4, 22, 10, 20);
        double valor = estacionamento.calcularValor(entrada, saida, Cliente.COMUM);
        assertEquals(0.0, valor); // Esperado: cortesia para COMUM
    }

    @Test
    void calcularValorCortesiaVIP() {
        LocalDateTime entrada = LocalDateTime.of(2025, 4, 22, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2025, 4, 22, 10, 20);
        double valor = estacionamento.calcularValor(entrada, saida, Cliente.VIP);
        assertEquals(0.0, valor); // Esperado: cortesia para VIP
    }

    // **Até 1 Hora**
    @Test
    void calcularValorAteUmaHoraComum() {
        LocalDateTime entrada = LocalDateTime.of(2025, 4, 22, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2025, 4, 22, 10, 59);
        double valor = estacionamento.calcularValor(entrada, saida, Cliente.COMUM);
        assertEquals(9.0, valor); // Esperado: tarifa para até 1 hora, COMUM
    }

    @Test
    void calcularValorAteUmaHoraVIP() {
        LocalDateTime entrada = LocalDateTime.of(2025, 4, 22, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2025, 4, 22, 10, 59);
        double valor = estacionamento.calcularValor(entrada, saida, Cliente.VIP);
        assertEquals(4.5, valor); // Esperado: tarifa para até 1 hora, VIP
    }

    // **Maior que 1 Hora**
    @Test
    void calcularValorMaiorUmaHoraComum() {
        LocalDateTime entrada = LocalDateTime.of(2025, 4, 22, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2025, 4, 22, 12, 45);
        double valor = estacionamento.calcularValor(entrada, saida, Cliente.COMUM);
        assertEquals(20.10, valor); // Esperado: tarifa para mais de 1 hora, COMUM
    }

    @Test
    void calcularValorMaiorUmaHoraVIP() {
        LocalDateTime entrada = LocalDateTime.of(2025, 4, 22, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2025, 4, 22, 12, 45);
        double valor = estacionamento.calcularValor(entrada, saida, Cliente.VIP);
        assertEquals(10.05, valor); // Esperado: tarifa para mais de 1 hora, VIP
    }

    // **Pernoite**
    @Test
    void calcularValorPernoiteComum() {
        LocalDateTime entrada = LocalDateTime.of(2025, 4, 22, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2025, 4, 23, 10, 20);
        double valor = estacionamento.calcularValor(entrada, saida, Cliente.COMUM);
        assertEquals(50.0, valor); // Esperado: tarifa de pernoite, COMUM
    }

    @Test
    void calcularValorPernoiteVIP() {
        LocalDateTime entrada = LocalDateTime.of(2025, 4, 22, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2025, 4, 23, 10, 20);
        double valor = estacionamento.calcularValor(entrada, saida, Cliente.VIP);
        assertEquals(25.0, valor); // Esperado: tarifa de pernoite, VIP
    }

    // **Testando entradas fora do horário**
    @Test
    void entradaAntesDaAbertura() {
        assertThrows(IllegalArgumentException.class, () -> estacionamento.calcularValor(
                LocalDateTime.of(2025, 4, 22, 7, 59),
                LocalDateTime.of(2025, 4, 22, 12, 45),
                Cliente.COMUM
        ));
    }

    @Test
    void entradaDepoisDoFechamento() {
        assertThrows(IllegalArgumentException.class, () -> estacionamento.calcularValor(
                LocalDateTime.of(2025, 4, 23, 1, 59), // Entrada depois de 23:59
                LocalDateTime.of(2025, 4, 23, 10, 0),
                Cliente.COMUM
        ));
    }

    @Test
    void saidaAntesDasOitoHoras() {
        assertThrows(IllegalArgumentException.class, () -> estacionamento.calcularValor(
                LocalDateTime.of(2025, 4, 22, 7, 30), // Saída antes das 08:00
                LocalDateTime.of(2025, 4, 22, 7, 59),
                Cliente.COMUM
        ));
    }

    @Test
    void saidaDepoisDoFechamento() {
        assertThrows(IllegalArgumentException.class, () -> estacionamento.calcularValor(
                LocalDateTime.of(2025, 4, 22, 20, 0), // Entrada às 20:00
                LocalDateTime.of(2025, 4, 23, 3, 30), // Saída entre 02:00 e 07:59
                Cliente.COMUM
        ));
    }

    @Test
    void saidaAntesDaEntrada() {
        assertThrows(IllegalArgumentException.class, () -> estacionamento.calcularValor(
                LocalDateTime.of(2025, 4, 22, 15, 0),
                LocalDateTime.of(2025, 4, 22, 10, 0),
                Cliente.COMUM
        ));
    }

    // Os testes parametrizados, sla se é isso mesmo, pesquisei no YouTube e vi esse jeito de fazer, ai deu erro e pedi pro GPT arrumar e ficou assim.
    // teste parametrizado dos valores
    @ParameterizedTest
    @CsvSource({
            "2025-04-22T10:00, 2025-04-22T10:20, 0.0, COMUM", // Cortesia COMUM
            "2025-04-22T10:00, 2025-04-22T10:20, 0.0, VIP", // Cortesia VIP
            "2025-04-22T10:00, 2025-04-22T10:59, 9.0, COMUM", // Até 1 hora COMUM
            "2025-04-22T10:00, 2025-04-22T10:59, 4.5, VIP", // Até 1 hora VIP
            "2025-04-22T10:00, 2025-04-22T12:45, 20.10, COMUM", // Maior que 1 hora COMUM
            "2025-04-22T10:00, 2025-04-22T12:45, 10.05, VIP", // Maior que 1 hora VIP
            "2025-04-22T10:00, 2025-04-23T08:20, 50.0, COMUM", // Pernoite COMUM
            "2025-04-22T10:00, 2025-04-23T08:20, 25.0, VIP"  // Pernoite VIP
    })
    void calcularValorComParametro(String entrada, String saida, double valorEsperado, String tipoCliente) {
        LocalDateTime entradaDate = LocalDateTime.parse(entrada, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime saidaDate = LocalDateTime.parse(saida, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Cliente cliente = Cliente.valueOf(tipoCliente);

        double valor = estacionamento.calcularValor(entradaDate, saidaDate, cliente);
        assertEquals(valorEsperado, valor, 0.01);
    }

    //teste parametrizado dos horários
    @ParameterizedTest
    @CsvSource({
            "07:59, 12:45, COMUM", // Entrada antes da abertura
            "23:59, 10:00, COMUM", // Entrada depois do fechamento
            "07:30, 07:59, COMUM", // Saída antes das 08:00
            "20:00, 03:30, COMUM"  // Saída depois do fechamento
    })
    void entradaOuSaidaForaDoHorario(String entrada, String saida, String tipoCliente) {
        LocalDateTime entradaDate = LocalDateTime.parse("2025-04-22T" + entrada + ":00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime saidaDate = LocalDateTime.parse("2025-04-22T" + saida + ":00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Cliente cliente = Cliente.valueOf(tipoCliente);

        assertThrows(IllegalArgumentException.class, () -> estacionamento.calcularValor(entradaDate, saidaDate, cliente));
    }


}
