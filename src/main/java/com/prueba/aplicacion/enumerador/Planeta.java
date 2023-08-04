package com.prueba.aplicacion.enumerador;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum Planeta {
    Ferengi(BigDecimal.valueOf(1L),BigDecimal.valueOf(500L),SentidoGiro.HORARIO),
    Betasoide(BigDecimal.valueOf(3L),BigDecimal.valueOf(2000L),SentidoGiro.HORARIO),
    Vulcano(BigDecimal.valueOf(5L),BigDecimal.valueOf(1000L),SentidoGiro.ANTIHORARIO);


    private BigDecimal velocidadAngular;
    private BigDecimal radio;
    private SentidoGiro giro;

    private Planeta(BigDecimal velocidadAngular , BigDecimal radio , SentidoGiro giro){
        this.velocidadAngular=velocidadAngular;
        this.radio=radio;
        this.giro=giro;
    }
}
