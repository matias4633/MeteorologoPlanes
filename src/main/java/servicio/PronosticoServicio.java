package servicio;

import modelo.Pronostico;

import java.time.LocalDate;

public interface PronosticoServicio {

    Pronostico getPronostico(LocalDate fecha);

    Pronostico savePronostico(Pronostico pronostico);


}
