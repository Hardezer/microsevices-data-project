package com.example.demo.servicesTest;

import com.example.demo.entities.*;
import com.example.demo.models.Cuota;
import com.example.demo.models.Reporte;
import com.example.demo.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class EstudianteService {
    private final EstudianteRepository estudianteRepository;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;


    }

    public List<Estudiante> getAllEstudiantes() {
        return estudianteRepository.findAll();
    }

    public Estudiante updateEstudiante(String rut, Estudiante estudiante) {
        Optional<Estudiante> estudianteOptional = estudianteRepository.findAllByRut(rut);
        if (estudianteOptional.isPresent()){
            estudiante.setRut(rut);
            return estudianteRepository.save(estudiante);
        }
        return null;
    }

    public void generarCuotas(Estudiante estudiante, String metodoPago) {
        int numCuotas = estudiante.getNumeroCuotas();
        double arancelTotal = 1500000.0;

        if (metodoPago.equals("Al Contado")) {
            arancelTotal *= 0.5;

            // Construir el objeto con los datos a enviar para crear cuotas
            Map<String, Object> request = new HashMap<>();
            request.put("numCuotas", numCuotas);
            request.put("rutEstudiante", estudiante.getRut());
            request.put("arancelTotal", arancelTotal);
            request.put("metodoPago", metodoPago);

            restTemplate.postForLocation(
                    "http://cuota-service/Cuota/crearCuotaContado",
                    new HttpEntity<>(request)
            );
        } else {
            // Hacer la llamada HTTP al microservicio Cuota para obtener el descuento
            ResponseEntity<Double> responseDescuento = restTemplate.postForEntity(
                    "http://cuota-service/Cuota/descuentoColegio",
                    estudiante,
                    Double.class
            );
            double descuentoTotal = responseDescuento.getBody();

            double arancelConDescuento = arancelTotal * (1 - descuentoTotal);
            double montoCuota = Math.round(arancelConDescuento / numCuotas);

            // Construir el objeto con los datos a enviar para crear cuotas
            Map<String, Object> requestCrearCuotas = new HashMap<>();
            requestCrearCuotas.put("numCuotas", numCuotas);
            requestCrearCuotas.put("rutEstudiante", estudiante.getRut());
            requestCrearCuotas.put("montoCuota", montoCuota);
            requestCrearCuotas.put("metodoPago", metodoPago);

            restTemplate.postForLocation(
                    "http://cuota-service/Cuota/crearCuotas",
                    new HttpEntity<>(requestCrearCuotas)
            );
        }
    }




    public void crearEstudianteYCuotas(Estudiante estudiante) {
        estudianteRepository.save(estudiante);
        generarCuotas(estudiante, estudiante.getMetodoPago());
    }


    public List<Reporte> getReportesEstudiantes(){
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        List<Reporte> reportes = new ArrayList<>();
        reportes = getAllReportes(estudiantes, reportes );
        return reportes;
    }


    public List<Reporte> getAllReportes(List<Estudiante> estudiantes, List<Reporte> reportes) {
        for (Estudiante estudiante : estudiantes) {
            reportes.add(generarReporte(estudiante));
        }
        return reportes;
    }

    public Reporte generarReporte(Estudiante estudiante) {
        String rut = estudiante.getRut();

        String cuotaUrl = "http://cuota-service/Cuota/costoArancel/" + rut;
        ResponseEntity<Integer> response = restTemplate.getForEntity(cuotaUrl, Integer.class);
        int costoArancel = response.getBody() != null ? response.getBody() : 0;

        List<Examen> examenes = new ArrayList<>();
        try {
            String examenUrl = "http://examen-service/Examen/byRut/" + rut;
            ResponseEntity<List<Examen>> examenResponse = restTemplate.exchange(examenUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Examen>>() {});
            examenes = examenResponse.getBody() != null ? examenResponse.getBody() : new ArrayList<>();
        } catch (HttpClientErrorException.NotFound e) {
        }

        String promedioUrl = "http://examen-service/Examen/promedioByRut/" + rut;
        ResponseEntity<Double> promedioResponse = restTemplate.getForEntity(promedioUrl, Double.class);
        double promedio = promedioResponse.getBody() != null ? promedioResponse.getBody() : 0.0;

        String cuotasUrl = "http://cuota-service/Cuota/byRutAndEstadoPago?rut=" + rut + "&estadoPago=Pagada";
        ResponseEntity<List<Cuota>> cuotasResponse = restTemplate.exchange(cuotasUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Cuota>>() {});
        List<Cuota> cuotasPagadasList = cuotasResponse.getBody() != null ? cuotasResponse.getBody() : new ArrayList<>();
        int cuotasPagadas = cuotasPagadasList.size();

        String montoPagadoUrl = "http://cuota-service/Cuota/sumMontoByRutAndEstadoPago?rut=" + rut + "&estadoPago=Pagada";
        ResponseEntity<Double> montoPagadoResponse = restTemplate.getForEntity(montoPagadoUrl, Double.class);
        double montoPagado = montoPagadoResponse.getBody() != null ? montoPagadoResponse.getBody() : 0.0;

        String ultimaCuotaUrl = "http://cuota-service/Cuota/lastPaymentByRut/" + rut;
        Optional<Cuota> ultimaCuotaPagoOptional = Optional.empty();
        try {
            ResponseEntity<Cuota> ultimaCuotaResponse = restTemplate.getForEntity(ultimaCuotaUrl, Cuota.class);
            ultimaCuotaPagoOptional = Optional.ofNullable(ultimaCuotaResponse.getBody());
        } catch (HttpClientErrorException.NotFound e) {
        }

        String cuotasUrl2 = "http://cuota-service/Cuota/byRutAndEstadoPago?rut=" + rut + "&estadoPago=Atrasada";
        ResponseEntity<List<Cuota>> cuotasResponse2 = restTemplate.exchange(cuotasUrl2, HttpMethod.GET, null, new ParameterizedTypeReference<List<Cuota>>() {});
        List<Cuota> cuotasPagadasList2 = cuotasResponse2.getBody() != null ? cuotasResponse2.getBody() : new ArrayList<>();
        int cuotasAtrasadas = cuotasPagadasList2.size();

        LocalDate ultimoPago = ultimaCuotaPagoOptional.map(Cuota::getFechaPago).orElse(null);
        return new Reporte(
                rut,
                estudiante.getApellidos(),
                estudiante.getNombres(),
                examenes.size(),
                promedio,
                costoArancel,
                estudiante.getMetodoPago(),
                estudiante.getNumeroCuotas(),
                cuotasPagadas,
                (int) montoPagado, // Convertimos el monto pagado a int
                ultimoPago,
                costoArancel - (int) montoPagado,
                cuotasAtrasadas
        );
    }

    public void deleteByRut(String Rut) {
        estudianteRepository.deleteByRut(Rut);
    }

    public Estudiante findByRut(String rut) {
        return estudianteRepository.findByRut(rut);
    }

    public Optional<Estudiante> findAllByRut(String rut) {
        return estudianteRepository.findAllByRut(rut);
    }

}
