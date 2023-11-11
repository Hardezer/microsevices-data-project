package com.example.demo.controllers;
import com.example.demo.entities.*;
import com.example.demo.models.Reporte;
import com.example.demo.servicesTest.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Estudiante")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @Autowired
    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }


    @GetMapping()
    public ResponseEntity<List<Estudiante>> getAllEstudiantes() {
        List<Estudiante> estudiantes = estudianteService.getAllEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }
    @GetMapping("/byRut/{rut}")
    public Estudiante getEstudianteByRut(@PathVariable String rut) {
        return estudianteService.findByRut(rut);
    }
    @PostMapping("/crearEstudiante")
    public String crearEstudiante(@RequestBody Estudiante estudiante) {
        try {
            estudianteService.crearEstudianteYCuotas(estudiante);
            return "redirect:/Estudiante/registroEstudiante";
        } catch (Exception e) {
            e.printStackTrace(); // Esto imprimirá el stack trace del error
            return "error";
        }
    }

    @GetMapping("/reporteEstudiantes")
    public String getReportesEstudiantesByRut(Model model) {
        try {
            model.addAttribute("estudiantes", estudianteService.getAllEstudiantes());
            return "reporteEstudiantes";
        } catch (Exception e) {
            e.printStackTrace(); // Añade esta línea
            return "error";
        }
    }



    @GetMapping("/reporte")
    public ResponseEntity<List<Reporte>> getReportesByRut() {
        try {
            List<Reporte> reportes = estudianteService.getReportesEstudiantes();
            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/update/{rut}")
    public ResponseEntity<Estudiante> updateEstudiante(@PathVariable String rut, @RequestBody Estudiante estudiante) {
        Estudiante estudianteActualizado = estudianteService.updateEstudiante(rut, estudiante);
        if (estudianteActualizado != null) {
            return ResponseEntity.ok(estudianteActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{rut}")
    public ResponseEntity<String> deleteEstudiante(@PathVariable String rut) {
        estudianteService.deleteByRut(rut);
        return ResponseEntity.ok("Estudiante eliminado correctamente");
    }

}
