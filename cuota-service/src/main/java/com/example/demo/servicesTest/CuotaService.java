package com.example.demo.servicesTest;

import com.example.demo.entities.Cuota;
import com.example.demo.models.Estudiante;
import com.example.demo.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CuotaService {

    private final CuotaRepository cuotaRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    public CuotaService(CuotaRepository cuotaRepository) {
        this.cuotaRepository = cuotaRepository;

    }

    public double descuentoPorTipoDeColegio(String tipoColegioProcedencia) {
        switch (tipoColegioProcedencia) {
            case "Municipal":
                return 0.20;
            case "Subvencionado":
                return 0.10;
            case "Privado":
                return 0.0;
            default:
                return 0.0; // O manejar una excepción o error según lo prefieras
        }
    }

    public double descuentoPorAñoDeEgreso(int añoEgresoColegio) {
        int añosDesdeEgreso = LocalDate.now().getYear() - añoEgresoColegio;
        if (añosDesdeEgreso < 1) {
            return 0.15; // 15% descuento
        } else if (añosDesdeEgreso <= 2) {
            return 0.08; // 8% descuento
        } else if (añosDesdeEgreso <= 4) {
            return 0.04; // 4% descuento
        }
        return 0.0;
    }

    public double descuentoColegio(Estudiante estudiante) {
        double descuentoTotal = 0.0;
        descuentoTotal += descuentoPorTipoDeColegio(estudiante.getTipoColegioProcedencia());
        descuentoTotal += descuentoPorAñoDeEgreso(estudiante.getAnoEgresoColegio());
        return descuentoTotal;
    }

    public void saveAll(List<Cuota> cuotas, String metodoPago){
        for (Cuota cuota : cuotas) {
            if (metodoPago.equals("Al Contado")) {
                cuota.setFechaPago(LocalDate.now());
            }
        }
        cuotaRepository.saveAll(cuotas);
    }

    public List<Cuota> crearCuotas(int numCuotas, String rutEstudiante, double montoCuota, String metodoPago) {
        List<Cuota> cuotas = new ArrayList<>();

        // Hacer la llamada HTTP al microservicio Estudiante
        String estudianteUrl = "http://estudiante-service/Estudiante/byRut/" + rutEstudiante;
        ResponseEntity<Estudiante> estudianteResponseOtraVez = restTemplate.getForEntity(estudianteUrl, Estudiante.class);
        Estudiante estudiante = estudianteResponseOtraVez.getBody();

        if ("Al Contado".equals(metodoPago)) {
            cuotas.add(crearCuota(estudiante, montoCuota, 1, LocalDate.now().plusYears(1), "Pagada"));
        } else {
            for (int i = 1; i <= numCuotas; i++) {
                Cuota aux = crearCuota(estudiante, montoCuota, i, LocalDate.now().plusMonths(i).withDayOfMonth(10), "Pendiente");
                cuotas.add(aux);

            }
        }

        return cuotas;
    }

    @Scheduled(cron = "0 0 0 11 * ?")  // Ejecuta el día 11 de cada mes a la medianoche
    public void cuotaVencida() {
        List<Cuota> cuotas = cuotaRepository.findByEstadoPagoNot("Pagada");
        int maxMonthsLate = marcarCuotasAtrasadas(cuotas);
        double interestRate = calculateInterestRate(maxMonthsLate);
        aplicarInteresCuotas(cuotas, interestRate);
        cuotaRepository.saveAll(cuotas);
    }

    private int marcarCuotasAtrasadas(List<Cuota> cuotas) {
        int maxMonthsLate = 0;

        for (Cuota cuota : cuotas) {
            if (cuota.getFechaVencimiento().isBefore(LocalDate.now()) && !"Atrasada".equals(cuota.getEstadoPago())) {
                cuota.setEstadoPago("Atrasada");
                int monthsLate = Period.between(cuota.getFechaVencimiento(), LocalDate.now()).getMonths();
                if (monthsLate > maxMonthsLate) {
                    maxMonthsLate = monthsLate;
                }
            }
        }

        return maxMonthsLate;
    }

    public void aplicarInteresCuotas(List<Cuota> cuotas, double interestRate) {
        for (Cuota cuota : cuotas) {
            if (!"Pagada".equals(cuota.getEstadoPago())) {
                cuota.setMontoCuota(cuota.getMontoCuota() + cuota.getMontoCuota() * interestRate);
            }
        }
    }

    private double calculateInterestRate(int monthsLate) {
        if (monthsLate == 0) return 0;
        if (monthsLate == 1) return 0.03;
        if (monthsLate == 2) return 0.06;
        if (monthsLate == 3) return 0.09;
        return 0.15;
    }
    private Cuota crearCuota(Estudiante estudiante, double monto, int numero, LocalDate fechaVencimiento, String estado) {
        Cuota cuota = new Cuota();
        cuota.setRutEstudiante(estudiante.getRut());
        cuota.setMontoCuota(monto);
        cuota.setNumeroCuota(numero);
        cuota.setFechaVencimiento(fechaVencimiento);
        cuota.setEstadoPago(estado);
        return cuota;
    }

    public void actualizarCuotaByPromedio(double promedio, Cuota cuota){
        if(promedio>=950){
            cuota.setMontoCuota(cuota.getMontoCuota() * 0.9);
        } else if (promedio>=900) {
            cuota.setMontoCuota(cuota.getMontoCuota() * 0.95);
        }else if(promedio>=850){
            cuota.setMontoCuota(cuota.getMontoCuota() * 0.98);
        }
    }

    public void pagarCuota(Long cuotaId) {
        Cuota cuota = cuotaRepository.findById(cuotaId).orElseThrow(() -> new EntityNotFoundException("Cuota no encontrada con ID: " + cuotaId));
        if (List.of("Pendiente", "Atrasada").contains(cuota.getEstadoPago())) {
            cuota.setFechaPago(LocalDate.now());
            cuota.setEstadoPago("Pagada");
            cuotaRepository.save(cuota);
        }
    }

    public List<Cuota> getCuotasByRut(String rut) {
        return cuotaRepository.findByRutEstudiante(rut);
    }

    public List<Cuota> getAllCuotas() {
        return cuotaRepository.findAll();
    }

    public Cuota updateCuota(Long id, Cuota cuota) {
        Optional<Cuota> cuotaOptional = cuotaRepository.findById(id);
        if (cuotaOptional.isPresent()){
            cuota.setId(id);
            return cuotaRepository.save(cuota);
        }
        return null;
    }

    public <S extends Cuota> S save(S entity) {
        return cuotaRepository.save(entity);
    }

    public Optional<Cuota> findById(Long id){
        return cuotaRepository.findById(id);
    }

    public List<Cuota> findByEstudiante_RutAndEstadoPago(String rut, String estadoPago) {
        return cuotaRepository.findByRutEstudianteAndEstadoPago(rut, estadoPago);
    }

    public Double findSumMontoCuotaByEstudiante_RutAndEstadoPago(String rut) {
        return cuotaRepository.findSumMontoCuotaByEstudiante_RutAndEstadoPago(rut);
    }

    public int obtenerCostoArancelByRut(String rut) {
        return cuotaRepository.obtenerCostoArancelByRut(rut);
    }


    public Optional<Cuota> findTopByEstudiante_RutOrderByFechaPagoDesc(String rut){
        return cuotaRepository.findTopByEstudiante_RutOrderByFechaPagoDesc(rut);
    }
}
