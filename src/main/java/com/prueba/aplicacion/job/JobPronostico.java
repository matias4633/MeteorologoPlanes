package com.prueba.aplicacion.job;

import com.prueba.aplicacion.datos.ModeloDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class JobPronostico {
    private ModeloDatos datos;
    @Autowired
    public JobPronostico(ModeloDatos datos) {
        this.datos = datos;
    }

    @Scheduled(cron = "0 0 1 1 1/10 ?") //Ejecutara una vez cada 10 años. Para pruebas reemplazar con este, Ejecutara cada 30seg @Scheduled(cron = "*/30 * * * * *")
    public void calcularPronosticos(){
        datos.procesarPrediccionDelClima();
    }
}
