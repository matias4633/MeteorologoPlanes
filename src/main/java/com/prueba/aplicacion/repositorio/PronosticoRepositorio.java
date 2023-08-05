package com.prueba.aplicacion.repositorio;

import com.prueba.aplicacion.enumerador.TipoClima;
import com.prueba.aplicacion.modelo.Pronostico;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface PronosticoRepositorio extends MongoRepository<Pronostico,String> {
    Pronostico findByFecha(LocalDate fecha);

    List<Pronostico> findByActualEquals(Boolean valor);

    List<Pronostico> findByClimaEquals(TipoClima clima);
}
