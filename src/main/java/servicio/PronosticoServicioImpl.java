package servicio;

import modelo.Pronostico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositorio.PronosticoRepositorio;

import java.time.LocalDate;

@Service
public class PronosticoServicioImpl implements PronosticoServicio{
    @Autowired
    private PronosticoRepositorio repositorio;

    @Override
    public Pronostico getPronostico(LocalDate fecha) {
        return repositorio.findByFecha(fecha);
    }

    @Override
    public Pronostico savePronostico(Pronostico pronostico) {
       repositorio.save(pronostico);
       return pronostico;
    }
}
