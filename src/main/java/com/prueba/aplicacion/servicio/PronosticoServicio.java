package com.prueba.aplicacion.servicio;

import com.prueba.aplicacion.enumerador.TipoClima;
import com.prueba.aplicacion.modelo.Pronostico;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface PronosticoServicio {

    Optional<Pronostico> getPronostico(LocalDate fecha);

    Pronostico savePronostico(Pronostico pronostico);

    void insertarDocumentos(List<Pronostico> documentosParaInsertar);
    //void marcarParaActualizacionBulk();

    void marcarParaActualizacion();

    List<Pronostico> getPorClima(TipoClima clima);

    void borrarPronosticosNoActuales();
}
