package com.example.demo.entities;

import com.example.demo.models.Estudiante;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Cuota")
public class Cuota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rutEstudiante;
    private int numeroCuota;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;
    private double montoCuota;
    private String estadoPago;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPago;

}
