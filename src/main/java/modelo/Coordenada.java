package modelo;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Coordenada {
    private BigDecimal x;
    private BigDecimal y;

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }
}
