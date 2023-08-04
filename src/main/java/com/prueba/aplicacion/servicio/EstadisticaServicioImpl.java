package com.prueba.aplicacion.servicio;

import com.prueba.aplicacion.modelo.Estadistica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.prueba.aplicacion.repositorio.EstadisticaRepositorio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EstadisticaServicioImpl implements EstadisticaServicio{
    @Autowired
    private EstadisticaRepositorio repositorio;

    @Override
    public Estadistica saveEstadistica(Estadistica estadistica) {
        repositorio.save(estadistica);
        return estadistica;
    }

    @Override
    public Optional<Estadistica> getEstadistica(LocalDate fecha) {
        return Optional.ofNullable(repositorio.findByFechaActualizacionEquals(fecha));
    }

    @Override
    public Optional<Estadistica> getUltimaEstadistica() {
       return Optional.ofNullable(repositorio.findMostRecentBeforeDateTime(LocalDateTime.now())) ;
    }
}
