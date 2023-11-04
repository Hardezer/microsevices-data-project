package com.example.demo.models;

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

public class Examen {

    private String rut_estudiante;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaExamen;
    private int puntajeObtenido;

}