package com.example.demo.repositories;

import com.example.demo.entities.Cuota;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CuotaRepository extends JpaRepository<Cuota, Long> {

    @Query(value = "SELECT * FROM cuota c WHERE c.rut_estudiante = :rut AND c.fecha_pago IS NOT NULL " +
            "ORDER BY c.fecha_pago DESC LIMIT 1", nativeQuery = true)
    Optional<Cuota> findTopByEstudiante_RutOrderByFechaPagoDesc(@Param("rut") String rut);

    List<Cuota> findByRutEstudiante(String rut);


    List<Cuota> findByEstadoPagoNot(String estadoPago);

    List<Cuota> findByRutEstudianteAndEstadoPago(String rut, String estadoPago);

    @Query("SELECT SUM(c.montoCuota) FROM Cuota c WHERE c.rutEstudiante = :rut AND c.estadoPago = 'Pagada'")
    Double findSumMontoCuotaByEstudiante_RutAndEstadoPago(@Param("rut") String rut);

    @Query("SELECT SUM(c.montoCuota) FROM Cuota c WHERE c.rutEstudiante = :rut AND c.estadoPago IN ('Pagada', 'Pendiente', 'Atrasada')")
    int obtenerCostoArancelByRut(@Param("rut") String rut);


}
