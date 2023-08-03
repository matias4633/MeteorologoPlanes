package modelo;

import enumerador.TipoClima;
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
     private String id;
     private TipoClima clima;
     private LocalDate fecha;
}
