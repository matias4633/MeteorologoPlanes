package com.prueba.aplicacion.servicio;

import com.prueba.aplicacion.enumerador.TipoClima;
import com.prueba.aplicacion.modelo.Pronostico;
import com.prueba.aplicacion.repositorio.PronosticoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PronosticoServicioImpl implements PronosticoServicio{
    @Autowired
    private PronosticoRepositorio repositorio;

    public PronosticoServicioImpl() {

    }

    @Override
    public Optional<Pronostico> getPronostico(LocalDate fecha) {
        return Optional.ofNullable(repositorio.findByFecha(fecha));
    }

    @Override
    public Pronostico savePronostico(Pronostico pronostico) {
       repositorio.save(pronostico);
       return pronostico;
    }
    public void insertarDocumentos(List<Pronostico> documentosParaInsertar) {
        repositorio.saveAll(documentosParaInsertar);
    }

    public void marcarParaActualizacion(){
        List<Pronostico> pronosticos = repositorio.findAll();
        for (Pronostico p :pronosticos) {
            p.setActual(false);
        }
        repositorio.saveAll(pronosticos);
    }
    @Override
    public List<Pronostico> getPorClima(TipoClima clima) {
        return repositorio.findByClimaEquals(clima);
    }

    public void borrarPronosticosNoActuales() {
        repositorio.deleteAll(repositorio.findByActualEquals(false));
    }
}
