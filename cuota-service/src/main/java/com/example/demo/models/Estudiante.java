package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class Estudiante {
    private String rut;
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
