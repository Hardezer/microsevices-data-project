package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class Estudiante {

    private String rut; // En lugar de rut_estudiante
    private String apellidos;
    private String nombres;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private String tipoColegioProcedencia;
    private String metodoPago;
    private String nombreColegio;
    private int anoEgresoColegio;
    private int numeroCuotas;

}
