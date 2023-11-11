package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reporte {
    private String rut;
    private String apellidos;
    private String nombres;
    private int examenesRendidos;
    private double promedioExamenes;
    private int costoArancel;
    private String tipoPago;
    private int numeroCuotas;
    private int cuotasPagadas;
    private int totalPagado;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ultimoPago;
    private int saldoPorPagar;
    private int cuotasAtrasadas;
}
