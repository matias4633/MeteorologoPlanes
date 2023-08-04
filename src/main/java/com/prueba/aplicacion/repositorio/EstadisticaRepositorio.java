package com.prueba.aplicacion.repositorio;

import com.prueba.aplicacion.modelo.Estadistica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EstadisticaRepositorio extends MongoRepository<Estadistica,String> {
    Estadistica findByFechaActualizacionEquals(LocalDate fecha);
    @Query("{'fechaActualizacion' : { $lte : ?0 }}")
    Estadistica findMostRecentBeforeDateTime(LocalDateTime fechaActualizacion);

}
