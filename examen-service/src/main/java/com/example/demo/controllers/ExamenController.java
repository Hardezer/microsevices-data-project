package com.example.demo.controllers;
import com.example.demo.entities.Examen;
import com.example.demo.servicesTest.ExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/Examen")
public class ExamenController {

    private final ExamenService examenService;

    @Autowired
    public ExamenController(ExamenService examenService) {
        this.examenService = examenService;
    }

    @GetMapping("/importarNotas")
    public String importarNotasForm() {
        return "importarNotas"; // Devuelve el nombre del archivo HTML sin la extensión
    }

    @PostMapping("/importarNotas")
    public String importarNotas(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String message = examenService.importarNotas(file);
            model.addAttribute("message", message);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "importarNotas";
    }

    @GetMapping("/promedioByRut/{rut}")
    public ResponseEntity<Double> getPromedioExamenesByRut(@PathVariable String rut) {
        Double promedio = examenService.getPromedioExamenesByRut(rut);
        if(promedio == null) {
            return new ResponseEntity<>(0.0, HttpStatus.OK);
        }
        return new ResponseEntity<>(promedio, HttpStatus.OK);
    }

    @GetMapping("/obtenerExamenes")
    public ResponseEntity<List<Examen>> getAllExamen() {
        List<Examen> examenes = examenService.getAllExamenes();
        return ResponseEntity.ok(examenes);
    }

    @GetMapping("/byRut/{rut}")
    public ResponseEntity<List<Examen>> getExamenesByRut(@PathVariable String rut) {
        List<Examen> examenes = examenService.findByEstudiante_Rut(rut);
        if (examenes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(examenes, HttpStatus.OK);
    }

    @PostMapping("/crearExamen")
    public ResponseEntity<Examen> saveExamen(@RequestBody Examen examen){
        Examen obtenerExamen = examenService.save(examen);
        return ResponseEntity.ok(obtenerExamen);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Examen> updateExamen(@PathVariable Long id, @RequestBody Examen examen) {
        Examen examenActualizado = examenService.updateExamen(id, examen);
        if (examenActualizado != null) {
            return ResponseEntity.ok(examenActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
