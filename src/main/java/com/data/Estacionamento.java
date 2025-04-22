package com.data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Estacionamento {

    private LocalTime horaAbertura = LocalTime.of(8, 0, 0);
    private LocalTime horaFechamento = LocalTime.of(2, 0, 0);

    private LocalTime horaEntrada = LocalTime.of(8, 0, 0);
    private LocalTime horaMaxSaida = LocalTime.of(23, 59, 0);

    private final int CORTESIA = 20;
    private final double PERNOITE = 50;
    private final double VALOR_HORA = 9;
    private final double VALOR_HORA_EXTRA = 5.55;

    private Cliente cliente; //não sei se é melhor usar esse no metodo ao inves de passar o cliente por parametro

    public double calcularValor(LocalDateTime entrada, LocalDateTime saida, Cliente cliente){
        validarHorarios(entrada,saida);

        long minutos = Duration.between(entrada, saida).toMinutes();

        if (minutos <= CORTESIA) {
            return 0.0;
        }

        boolean isPernoite = saida.toLocalDate().isAfter(entrada.toLocalDate()) &&
                saida.toLocalTime().isAfter(horaEntrada);

        double valor;

        if (isPernoite) {
            long dias = Duration.between(entrada.toLocalDate().atTime(horaEntrada), saida).toDays();
            dias = Math.max(dias, 1);
            valor = dias * PERNOITE;
        } else {
            if (minutos <= 60) {
                valor = VALOR_HORA;
            } else {
                long horas = (long) Math.ceil(minutos / 60.0);
                valor = VALOR_HORA + (horas - 1) * VALOR_HORA_EXTRA;
            }
        }

        if (cliente == Cliente.VIP) {
            valor *= 0.5;
        }

        return valor;
    }

    public void validarHorarios(LocalDateTime entrada, LocalDateTime saida) {
        LocalTime horarioEntrada = entrada.toLocalTime();
        LocalTime horarioSaida = saida.toLocalTime();

        if (horarioEntrada.isBefore(horaAbertura) || horarioEntrada.isAfter(horaMaxSaida)) {
            throw new IllegalArgumentException("Entrada só é permitida entre 08:00 e 23:59");
        }

        boolean mesmaData = entrada.toLocalDate().equals(saida.toLocalDate());

        if (mesmaData && horarioSaida.isBefore(horaAbertura)) {
            throw new IllegalArgumentException("Saida não é permitida entre 02:00 e 07:59.");
        }

        if (!mesmaData && horarioSaida.isBefore(horaAbertura) && horarioSaida.isAfter(horaFechamento)) {
            throw new IllegalArgumentException("Saida não é permitida entre 02:00 e 07:59.");
        }
        if (saida.isBefore(entrada)) {
            throw new IllegalArgumentException("A saída não pode ser antes da entrada");
        }

    }
}
