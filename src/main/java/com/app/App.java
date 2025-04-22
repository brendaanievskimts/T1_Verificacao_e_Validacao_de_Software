package com.app;

import com.data.Cliente;
import com.data.Estacionamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Estacionamento estacionamento = new Estacionamento();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

        String resposta;

        do {
            try {
                System.out.println("\n=== Estacionamento do 32 ===");

                // Entrada
                System.out.print("Digite a data de entrada (dd-MM-yyyy): ");
                LocalDate dataEntrada = LocalDate.parse(sc.nextLine(), dateFormat);
                System.out.print("Digite o horário de entrada (HH:mm): ");
                LocalTime horaEntrada = LocalTime.parse(sc.nextLine(), timeFormat);
                LocalDateTime entrada = LocalDateTime.of(dataEntrada, horaEntrada); //junta os dois

                // Saída
                System.out.print("Digite a data de saída (dd-MM-yyyy): ");
                LocalDate dataSaida = LocalDate.parse(sc.nextLine(), dateFormat);
                System.out.print("Digite o horário de saída (HH:mm): ");
                LocalTime horaSaida = LocalTime.parse(sc.nextLine(), timeFormat);
                LocalDateTime saida = LocalDateTime.of(dataSaida, horaSaida);

                // Tipo de cliente
                System.out.print("Tipo de cliente (1 - COMUM, 2 - VIP): ");
                int tipo = Integer.parseInt(sc.nextLine());
                Cliente cliente = (tipo == 2) ? Cliente.VIP : Cliente.COMUM;

                double valor = estacionamento.calcularValor(entrada, saida, cliente);
                System.out.printf("Valor a pagar: R$ %.2f%n", valor);

            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

            System.out.println("\nEntrada/Saída de outro carro? (s/n)");
            resposta = sc.nextLine().trim().toLowerCase();

        } while (resposta.equals("s") || resposta.equals("sim"));

        System.out.println("Fechando estacionamento. Valeu!");
        sc.close();
    }
}
