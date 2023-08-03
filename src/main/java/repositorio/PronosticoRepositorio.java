package repositorio;

import modelo.Pronostico;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface PronosticoRepositorio extends MongoRepository<Pronostico,String> {
    Pronostico findByFecha(LocalDate fecha);
}
