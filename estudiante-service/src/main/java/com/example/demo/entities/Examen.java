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

public class Examen {


    private String rut_Estudiante;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaExamen;

    private int puntajeObtenido;

}