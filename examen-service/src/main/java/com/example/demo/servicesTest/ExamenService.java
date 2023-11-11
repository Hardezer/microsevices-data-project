package com.example.demo.servicesTest;

import com.example.demo.entities.Cuota;
import com.example.demo.entities.Estudiante;
import com.example.demo.entities.Examen;
import com.example.demo.repositories.ExamenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExamenService{

    private final ExamenRepository examenRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public ExamenService(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    public List<Examen> getAllExamenes() {
        return examenRepository.findAll();
    }

    public List<Examen> findByEstudiante_Rut(String rut) {
        return examenRepository.findByRut_estudiante(rut);
    }

    public Examen updateExamen(Long id, Examen examen) {
        Optional<Examen> examenOptional = examenRepository.findById(id);
        if (examenOptional.isPresent()){
            examen.setId(id);
            return examenRepository.save(examen);
        } else {
            return null;
        }
    }

    public String importarNotas(MultipartFile file) throws Exception{
        if (!file.isEmpty()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                boolean headerLineSkipped = false;
                while ((line = br.readLine()) != null) {
                    if (!headerLineSkipped) {
                        headerLineSkipped = true;
                        continue;
                    }

                    String[] parte = line.split(";");
                    if (parte.length == 3) {
                        String rut = parte[0].trim();
                        LocalDate fechaExamen = LocalDate.parse(parte[1].trim());
                        int puntajeObtenido = Integer.parseInt(parte[2].trim());

                        String estudianteUrl = "http://estudiante-service/Estudiante/byRut/" + rut;
                        ResponseEntity<Estudiante> estudianteResponse = restTemplate.getForEntity(estudianteUrl, Estudiante.class);
                        Estudiante estudiante = estudianteResponse.getBody();

                        crearExamen(estudiante, fechaExamen, puntajeObtenido);
                    }
                }
                return "Notas de exámenes importadas exitosamente.";
            } catch (IOException e) {
                throw new Exception("Error al cargar el archivo CSV.", e);
            }
        } else {
            throw new Exception("El archivo está vacío.");
        }
    }


    public void crearExamen(Estudiante estudiante, LocalDate fechaExamen, int puntajeObtenido){
        if (estudiante != null) {
            Examen examen = new Examen();
            examen.setRut(estudiante.getRut());
            examen.setFechaExamen(fechaExamen);
            examen.setPuntajeObtenido(puntajeObtenido);
            examenRepository.save(examen);
            actualizarDescuentoByExamen(examen);
        }
    }

    public void actualizarDescuentoByExamen(Examen examen){

        String cuotasUrl = "http://cuota-service/Cuota/byRutAndEstadoPago?rut=" + examen.getRut() + "&estadoPago=Pendiente";
        ResponseEntity<List<Cuota>> cuotasResponse = restTemplate.exchange(
                cuotasUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Cuota>>() {}
        );
        List<Cuota> cuotas = cuotasResponse.getBody();

        Double promedioExamenes = examenRepository.getPromedioExamenesByRut(examen.getRut());
        for (Cuota cuota : cuotas) {
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("cuotaId", cuota.getId());
            requestMap.put("promedio", promedioExamenes);

            String actualizarCuotaUrl = "http://cuota-service/Cuota/actualizarCuotaByPromedio";
            restTemplate.postForEntity(actualizarCuotaUrl, requestMap, Void.class);
        }
    }

    public Double getPromedioExamenesByRut(String rut){
        return examenRepository.getPromedioExamenesByRut(rut);
    }

    public <S extends Examen> S save(S entity) {
        return examenRepository.save(entity);
    }

}
