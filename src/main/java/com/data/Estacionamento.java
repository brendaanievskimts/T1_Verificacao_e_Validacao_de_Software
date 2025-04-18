package com.data;

import java.time.LocalTime;

public class Estacionamento {

    private LocalTime horaAbertura = LocalTime.of(8, 0, 0);
    private LocalTime horaFechamento = LocalTime.of(2, 0, 0);

    private LocalTime horaEntrada = LocalTime.of(8, 0, 0);
    private LocalTime horaMaxSaida = LocalTime.of(23, 59, 0);

    private final int CORTESIA = 20;
    private final double PERNOITE = 50;
    private final double VALOR_HORA = 9;

    private Cliente cliente;


}
