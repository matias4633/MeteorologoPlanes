package com.prueba.aplicacion.controlador;

import com.prueba.aplicacion.datos.ModeloDatos;
import com.prueba.aplicacion.modelo.Estadistica;
import com.prueba.aplicacion.modelo.Pronostico;
import com.prueba.aplicacion.servicio.EstadisticaServicio;
import com.prueba.aplicacion.servicio.PronosticoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/pronostico")
public class PronosticoController {
    private ModeloDatos datos;
    private PronosticoServicio pronosticoServicio;
    private EstadisticaServicio estadisticaServicio;
    @Autowired
    public PronosticoController(ModeloDatos datos, PronosticoServicio pronosticoServicio, EstadisticaServicio estadisticaServicio) {
        this.datos = datos;
        this.pronosticoServicio = pronosticoServicio;
        this.estadisticaServicio = estadisticaServicio;
    }
    @GetMapping()
    public ResponseEntity<Pronostico> getPronosticoPorFecha(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha){
        Optional<Pronostico> pronostico = null;
        if(fecha != null){
            pronostico = pronosticoServicio.getPronostico(fecha);
        }
        return ResponseEntity.ok(pronostico.orElse(new Pronostico()));
    }
    @GetMapping("/estadistica")
    public ResponseEntity<Estadistica> getUltimaEstadistica(){
        Optional<Estadistica> ultimaEstadistica = estadisticaServicio.getUltimaEstadistica();
        return ResponseEntity.ok(ultimaEstadistica.orElse(new Estadistica()));
    }

    @PostMapping("/proceso/run")
    public ResponseEntity<String> ejecutarProceso(){
        datos.correrProcesoPronosticos(); //Este proceso corre en otro hilo, de forma asincrona.
        String mensaje = "Proceso Iniciado";
        return ResponseEntity.ok(mensaje);
    }

}
