package modelo;

import enumerador.TipoClima;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "pronosticos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pronostico {
     @Id
     private String id;
     private TipoClima clima;
     private LocalDate fecha;
}
