package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cuota {

    private Long id;
    private String rut_estudiante;
    private int numeroCuota;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;
    private double montoCuota;
    private String estadoPago;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPago;

}
