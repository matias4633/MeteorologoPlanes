package com.prueba.aplicacion.servicio;

import com.prueba.aplicacion.modelo.Estadistica;

import java.time.LocalDate;
import java.util.Optional;

public interface EstadisticaServicio {
    Estadistica saveEstadistica(Estadistica estadistica);
    Optional<Estadistica> getEstadistica(LocalDate fecha);

    Optional<Estadistica> getUltimaEstadistica();
}
