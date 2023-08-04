package com.prueba.aplicacion.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prueba.aplicacion.enumerador.TipoClima;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "pronosticos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pronostico {
     @Id
     @JsonIgnore
     private String id;
     private TipoClima clima;
     private LocalDate fecha;
     @JsonIgnore
     private Boolean actual = true;
}
