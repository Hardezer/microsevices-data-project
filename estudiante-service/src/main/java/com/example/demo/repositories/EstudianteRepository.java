package com.example.demo.repositories;

import com.example.demo.entities.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, String> {
    
    void deleteByRut(String Rut);

    Estudiante findByRut(String rut);
    Optional<Estudiante> findAllByRut(String rut);


}
