package com.example.demo.controllers;

import com.example.demo.entities.Cuota;
import com.example.demo.models.Estudiante;
import com.example.demo.servicesTest.CuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/Cuota")
public class CuotaController {

    private final CuotaService cuotaService;
    @Autowired
    public CuotaController(CuotaService cuotaService) {
        this.cuotaService = cuotaService;
    }

    @PostMapping("/crearCuotaContado")
    public void crearCuotaContado(@RequestBody Map<String, Object> request) {
        int numCuotas = (int) request.get("numCuotas");
        String rutEstudiante = (String) request.get("rutEstudiante");
        double arancelTotal = (double) request.get("arancelTotal");
        String metodoPago = (String) request.get("metodoPago");

        cuotaService.saveAll(cuotaService.crearCuotas(numCuotas, rutEstudiante, arancelTotal, metodoPago),metodoPago);

    }

    @GetMapping("/costoArancel/{rut}")
    public ResponseEntity<Integer> getCostoArancelByRut(@PathVariable String rut) {
        int costoArancel = cuotaService.obtenerCostoArancelByRut(rut);
        return new ResponseEntity<>(costoArancel, HttpStatus.OK);
    }

    @PostMapping("/crearCuotas")
    public void crearCuotas(@RequestBody Map<String, Object> request) {
        int numCuotas = (int) request.get("numCuotas");
        String rutEstudiante = (String) request.get("rutEstudiante");
        double montoCuota = (double) request.get("montoCuota");
        String metodoPago = (String) request.get("metodoPago");
        cuotaService.saveAll(cuotaService.crearCuotas(numCuotas, rutEstudiante, montoCuota, metodoPago), metodoPago);
    }

    @PostMapping("/save")
    public Cuota saveCuota(@RequestBody Cuota cuota) {
        return cuotaService.save(cuota);
    }


    @PostMapping("/descuentoColegio")
    public double getDescuentoColegio(@RequestBody Estudiante estudiante) {
        return cuotaService.descuentoColegio(estudiante);
    }

    @GetMapping("/byRut")
    public String showCuotaForm(Model model) {
        model.addAttribute("rut", ""); // Inicializa el campo rut
        return "buscarCuotas";
    }
    @PostMapping("/byRut")
    public ResponseEntity<List<Cuota>> getCuotasByRut(@RequestBody Map<String, String> body) {
        String rut = body.get("rut");
        try {
            List<Cuota> cuotas = cuotaService.getCuotasByRut(rut);
            return new ResponseEntity<>(cuotas, HttpStatus.OK);
        } catch (Exception e) {
            // Podrías manejar el error de manera más específica y retornar un mensaje apropiado
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping()
    public ResponseEntity<List<Cuota>> getAllCuotas() {
        try {
            List<Cuota> cuotas = cuotaService.getAllCuotas();
            return ResponseEntity.ok(cuotas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping("/pagarCuota")
    public ResponseEntity<String> pagarCuota(@RequestParam Long cuotaId) {
        try {
            cuotaService.pagarCuota(cuotaId);
            return new ResponseEntity<>("La cuota ha sido pagada exitosamente.", HttpStatus.OK);
        } catch (Exception e) {
            // Podrías manejar el error de manera más específica y retornar un mensaje apropiado
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/actualizarCuotaByPromedio")
    public ResponseEntity<Cuota> updateCuotaByPromedio(@RequestBody Map<String, Object> requestMap) {
        try {
            Long cuotaId = Long.parseLong(requestMap.get("cuotaId").toString());
            double promedio = Double.parseDouble(requestMap.get("promedio").toString());

            Optional<Cuota> cuota = cuotaService.findById(cuotaId);
            cuotaService.actualizarCuotaByPromedio(promedio, cuota.get());
            cuotaService.save(cuota.get());  // Asegúrate de guardar los cambios en la base de datos

            return ResponseEntity.ok(cuota.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/sumMontoByRutAndEstadoPago")
    public ResponseEntity<Double> getSumMontoCuotaByRutAndEstadoPago(@RequestParam String rut, @RequestParam String estadoPago) {
        Double sum = cuotaService.findSumMontoCuotaByEstudiante_RutAndEstadoPago(rut);
        if(sum == null) {
            return new ResponseEntity<>(0.0, HttpStatus.OK);
        }
        return new ResponseEntity<>(sum, HttpStatus.OK);
    }

    @GetMapping("/lastPaymentByRut/{rut}")
    public ResponseEntity<Cuota> getLastPaymentByRut(@PathVariable String rut) {
        Optional<Cuota> ultimaCuota = cuotaService.findTopByEstudiante_RutOrderByFechaPagoDesc(rut);
        if(!ultimaCuota.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ultimaCuota.get(), HttpStatus.OK);
    }



    @GetMapping("/byRutAndEstadoPago")
    public ResponseEntity<List<Cuota>> getCuotasByRutAndEstadoPago(@RequestParam String rut, @RequestParam String estadoPago) {
        try {
            List<Cuota> cuotas = cuotaService.findByEstudiante_RutAndEstadoPago(rut, estadoPago);
            return ResponseEntity.ok(cuotas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/update")
    public ResponseEntity<Cuota> updateCuota(Long id, @RequestBody Cuota cuota) {
        try {
            Cuota cuotaActualizado = cuotaService.updateCuota(id, cuota);
            if (cuotaActualizado != null) {
                return ResponseEntity.ok(cuotaActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
