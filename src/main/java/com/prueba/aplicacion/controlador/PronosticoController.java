package com.prueba.aplicacion.controlador;

import com.prueba.aplicacion.datos.ModeloDatos;
import com.prueba.aplicacion.enumerador.TipoClima;
import com.prueba.aplicacion.excepciones.NoEncontradoException;
import com.prueba.aplicacion.modelo.Estadistica;
import com.prueba.aplicacion.modelo.Pronostico;
import com.prueba.aplicacion.servicio.EstadisticaServicio;
import com.prueba.aplicacion.servicio.PronosticoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
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
        if(!pronostico.isPresent()){
            throw new NoEncontradoException("No se encontro un pronostico para la fecha solicitada");
        }
        return ResponseEntity.ok(pronostico.get());
    }
    @GetMapping("/buscar")
    public ResponseEntity<List<Pronostico>> getPronosticoPorClima(@RequestParam String clima){
        List<Pronostico> pronosticos = null;
        if(!clima.isEmpty()){
            pronosticos = pronosticoServicio.getPorClima(TipoClima.valueOf(clima));
        }
        if(pronosticos.isEmpty()){
            throw new NoEncontradoException("No se encontro pronosticos para el clima solicitado");
        }
        return ResponseEntity.ok(pronosticos);
    }
    @GetMapping("/estadistica")
    public ResponseEntity<Estadistica> getUltimaEstadistica(){
        Optional<Estadistica> ultimaEstadistica = estadisticaServicio.getUltimaEstadistica();
        if(!ultimaEstadistica.isPresent()){
            throw new NoEncontradoException("No se encontro un registro estadistica.");
        }
        return ResponseEntity.ok(ultimaEstadistica.get());
    }

    @PostMapping("/proceso/run")
    public ResponseEntity<String> ejecutarProceso(){
        datos.correrProcesoPronosticos(); //Este proceso corre en otro hilo, de forma asincrona.
        String mensaje = "Proceso Iniciado";
        return ResponseEntity.ok(mensaje);
    }

}
