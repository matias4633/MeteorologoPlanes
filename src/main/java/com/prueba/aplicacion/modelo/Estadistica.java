package com.prueba.aplicacion.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "estadistica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Estadistica {
    @JsonIgnore
    private String id;
    private Long sequia;
    private Long lluvia;
    private Long lluviaMaxima;
    private Long condicionesOptimas;
    private LocalDateTime fechaActualizacion;

    public Estadistica(Long sequia, Long lluvia ,Long lluviaMaxima , Long condicionesOptimas){
        this.sequia=sequia;
        this.lluvia=lluvia;
        this.lluviaMaxima=lluviaMaxima;
        this.condicionesOptimas=condicionesOptimas;
        this.fechaActualizacion = LocalDateTime.now();
    }
}
