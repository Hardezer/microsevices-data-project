package com.example.demo.repositories;

import com.example.demo.entities.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamenRepository extends JpaRepository<Examen, Long> {

    @Query("SELECT e FROM Examen e WHERE e.rut = ?1")
    List<Examen> findByRut_estudiante(String rut);


    @Query("SELECT AVG(e.puntajeObtenido) FROM Examen e WHERE e.rut = ?1")
    Double getPromedioExamenesByRut(String rut);
}
