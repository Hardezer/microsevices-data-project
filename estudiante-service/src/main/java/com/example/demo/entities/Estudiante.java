package com.example.demo.entities;

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
@Entity
@Table(name = "Estudiante")
public class Estudiante {
    @Id
    @Column(unique = true) // Asegura que los valores de rut sean únicos
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
