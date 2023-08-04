package com.prueba.aplicacion.datos;

import com.prueba.aplicacion.enumerador.Planeta;
import com.prueba.aplicacion.enumerador.SentidoGiro;
import com.prueba.aplicacion.enumerador.TipoClima;
import com.prueba.aplicacion.modelo.Coordenada;
import com.prueba.aplicacion.modelo.Estadistica;
import com.prueba.aplicacion.modelo.Pronostico;
import com.prueba.aplicacion.servicio.EstadisticaServicioImpl;
import com.prueba.aplicacion.servicio.PronosticoServicioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
@Component
public class ModeloDatos {
    private static final Long DIAS_EN_10_ANIOS = 3653L;

    private static final Coordenada SOL = new Coordenada(BigDecimal.ZERO, BigDecimal.ZERO);
    private static final BigDecimal TOLERANCIA = BigDecimal.valueOf(0.1); // Tolerancia
    private static final LocalDate DIA_0 = LocalDate.of(2023, 8, 7);
    //LO considero el dia cero donde los 3 planetas y sol estan alineados.
    //Con esto hace el modelo mas real.
    @Autowired
    private PronosticoServicioImpl pronosticoServicio;
    @Autowired
    private EstadisticaServicioImpl estadisticaServicio;
    @Autowired
    public ModeloDatos(PronosticoServicioImpl pronosticoServicio, EstadisticaServicioImpl estadisticaServicio) {
        this.pronosticoServicio = pronosticoServicio;
        this.estadisticaServicio = estadisticaServicio;
    }

    @Async
    public void correrProcesoPronosticos(){
        procesarPrediccionDelClima();
    }
    public void procesarPrediccionDelClima() {
        Map<LocalDate,BigDecimal> perimetros = new LinkedHashMap<>();
        LocalDate hoy = LocalDate.now();
        Long diasPasados = ChronoUnit.DAYS.between(DIA_0, hoy ) - 1 ;
        LocalDate diaActual = DIA_0.plusDays(diasPasados); //Dias pasados desde el DIA 0.
        List<Pronostico> pronosticos = new ArrayList<>();
        System.out.println(pronosticoServicio);
        pronosticoServicio.marcarParaActualizacion();
        //Marca los registros anteriores para que en caso de que se reprocese no existan dos pronosticos para la misma fecha.

        Long sequia = 0L;
        Long lluvia = 0L;
        Long lluviaMaxima = 0L;
        Long condicionesOptimas = 0L;

        for (Long index = 1L ;index <=DIAS_EN_10_ANIOS ; index++){
            Coordenada coordenada1 = getCoordenada(Planeta.Ferengi , BigDecimal.valueOf(diasPasados+index));
            Coordenada coordenada2 = getCoordenada(Planeta.Betasoide , BigDecimal.valueOf(diasPasados+index));
            Coordenada coordenada3 = getCoordenada(Planeta.Vulcano , BigDecimal.valueOf(diasPasados+index));
            Pronostico pronostico = new Pronostico();
            pronostico.setFecha(diaActual.plusDays(index));

            Boolean estanAlineadosEntreSi = verificarAlineacion(coordenada1,coordenada2,coordenada3);
            Boolean solDentroDelTriangulo = elSolEstaDentroDelTriangulo(coordenada1,coordenada2,coordenada3);
            Boolean estanAlineadosConElSOl = verificarAlineacion(coordenada1,coordenada2, SOL);
            BigDecimal perimetro = calcularPerimetro(coordenada1,coordenada2,coordenada3);

            if(estanAlineadosEntreSi){
                if(estanAlineadosConElSOl){
                    //sequia
                    pronostico.setClima(TipoClima.SEQUIA);
                    sequia++;
                }else{
                    //condiciones optimas.
                    pronostico.setClima(TipoClima.CONDICIONES_OPTIMAS);
                    condicionesOptimas++;
                }
            }else{
                //forman un triangulo.
                if(solDentroDelTriangulo){
                    //lluvia , Opt lluvia maxima.
                    //en este caso me importa el perimetro.
                    perimetros.put(diaActual.plusDays(index) , perimetro);
                    pronostico.setClima(TipoClima.LLUVIA);
                    lluvia++;
                }else{
                    //codiciones Optimas , supongo, no lo aclara.
                    pronostico.setClima(TipoClima.CONDICIONES_OPTIMAS);
                    condicionesOptimas++;
                }
            }
            pronosticos.add(pronostico);
        }

        // Ordenar el mapa por valores de BigDecimal de mayor a menor
        List<Map.Entry<LocalDate, BigDecimal>> listaOrdenada = new ArrayList<>(perimetros.entrySet());
        listaOrdenada.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Extraer el primer valor y compararlo con los siguientes valores hasta que la resta sea mayor a la toleracia,
        // para no recorrer toda la lista.

        BigDecimal maximoPerimetro = listaOrdenada.get(0).getValue();
        for (int index = 1; index < listaOrdenada.size(); index++) {
            BigDecimal valorActual = listaOrdenada.get(index).getValue();
            if (maximoPerimetro.subtract(valorActual).abs().compareTo(TOLERANCIA)==-1) {
                lluviaMaxima++;
                int finalIndex = index;
                Optional<Pronostico> pronosticoEncontrado = pronosticos.stream()
                        .filter(pronostico -> pronostico.getFecha().equals(listaOrdenada.get(finalIndex).getKey()))
                        .findFirst();
                pronosticoEncontrado.ifPresent(pronostico -> pronostico.setClima(TipoClima.MAXIMA_LLUVIA));
                lluvia--; //Para que en la estadistica no se cuente dos veces un dia de lluvia.
            }else{
                break;
            }
        }

        Estadistica estadistica = new Estadistica(sequia , lluvia ,lluviaMaxima,condicionesOptimas);
        estadisticaServicio.saveEstadistica(estadistica);
        pronosticoServicio.insertarDocumentos(pronosticos);
        pronosticoServicio.borrarPronosticosNoActuales();
        System.out.println(estadistica);
        System.out.println(pronosticos);
    }

    /**
     * Calcula el perimetro del triangulo formado por las coordenadas.
     * @param coordenada1
     * @param coordenada2
     * @param coordenada3
     * @return
     */
    private BigDecimal calcularPerimetro(Coordenada coordenada1, Coordenada coordenada2, Coordenada coordenada3) {
        BigDecimal lado1 = calcularDistancia(coordenada1, coordenada2);
        BigDecimal lado2 = calcularDistancia(coordenada2, coordenada3);
        BigDecimal lado3 = calcularDistancia(coordenada3, coordenada1);

        BigDecimal perimetro = lado1.add(lado2).add(lado3);
        System.out.println("Perimetro "+perimetro);
        return perimetro;
    }

    /**
     * Calcula el modulo del vector formado por dos puntos.
     * @param p1
     * @param p2
     * @return
     */
    public BigDecimal calcularDistancia(Coordenada p1, Coordenada p2) {
        BigDecimal x1 = p1.getX();
        BigDecimal y1 = p1.getY();
        BigDecimal x2 = p2.getX();
        BigDecimal y2 = p2.getY();

        BigDecimal distanciaX = x2.subtract(x1);
        BigDecimal distanciaY = y2.subtract(y1);

        return distanciaX.pow(2).add(distanciaY.pow(2)).sqrt(MathContext.DECIMAL128);
    }

    /**
     * Verifica si las coordenadas forman lineas rectas aproximadamente iguales, teniendo en cuenta una minima tolerancia.
     * @param coordenada1
     * @param coordenada2
     * @param coordenada3
     * @return
     */
    private Boolean verificarAlineacion(Coordenada coordenada1, Coordenada coordenada2, Coordenada coordenada3) {


        BigDecimal x1 = coordenada1.getX();
        BigDecimal y1 = coordenada1.getY();

        BigDecimal x2 = coordenada2.getX();
        BigDecimal y2 = coordenada2.getY();

        BigDecimal x3 = coordenada3.getX();
        BigDecimal y3 = coordenada3.getY();
        //(y2-y1)/(x2-x1) Formula de pendiente.
        BigDecimal pendiente1 = y2.subtract(y1).divide(x2.subtract(x1), MathContext.DECIMAL128).abs();
        BigDecimal pendiente2 = y3.subtract(y1).divide(x3.subtract(x1), MathContext.DECIMAL128).abs();
        System.out.println(pendiente1);
        System.out.println(pendiente2);

        // Verificamos si las pendientes son aproximadamente iguales.
        return pendiente1.subtract(pendiente2).abs().compareTo(TOLERANCIA) == -1;
    }

    /**
     * Verifica si el sol se encuentra dentro del triangulo formado por las 3 coordenadas.
     * @param coordenada1
     * @param coordenada2
     * @param coordenada3
     * @return
     */
    public boolean elSolEstaDentroDelTriangulo(Coordenada coordenada1, Coordenada coordenada2, Coordenada coordenada3) {
        double anguloTotal = 0.0;
        anguloTotal += calcularAngulo(coordenada1, coordenada2);
        anguloTotal += calcularAngulo(coordenada2, coordenada3);
        anguloTotal += calcularAngulo(coordenada3, coordenada1);

        // Verificamos si el punto (0,0) está dentro del triángulo comparando la suma de los ángulos con 360 grados
        return Math.abs(anguloTotal - 2 * Math.PI) < 1e-6;
    }

    /**
     * Calcula el angulo entre dos puntos.
     * @param p1
     * @param p2
     * @return
     */
    public double calcularAngulo(Coordenada p1, Coordenada p2) {
        BigDecimal x1 = p1.getX();
        BigDecimal y1 = p1.getY();
        BigDecimal x2 = p2.getX();
        BigDecimal y2 = p2.getY();

        BigDecimal productoPunto = x1.multiply(x2).add(y1.multiply(y2));
        BigDecimal magnitudP1 = x1.multiply(x1).add(y1.multiply(y1)).sqrt(MathContext.DECIMAL128);
        BigDecimal magnitudP2 = x2.multiply(x2).add(y2.multiply(y2)).sqrt(MathContext.DECIMAL128);

        BigDecimal cosenoAngulo = productoPunto.divide(magnitudP1.multiply(magnitudP2), MathContext.DECIMAL128);
        return Math.acos(cosenoAngulo.doubleValue());
    }

    /**
     * Calcula las coordenadas donde se encuentra el planeta pasados N dias.
     * @param planeta
     * @param dias
     * @return
     */
    public Coordenada getCoordenada(Planeta planeta, BigDecimal dias) {
        final BigDecimal GRADOS_A_RADIANES = BigDecimal.valueOf(Math.PI).divide(BigDecimal.valueOf(180L), MathContext.DECIMAL128);

        BigDecimal velocidadAngularEnRadianes = planeta.getVelocidadAngular().multiply(GRADOS_A_RADIANES);

        if (planeta.getGiro().equals(SentidoGiro.HORARIO)) {
            velocidadAngularEnRadianes = velocidadAngularEnRadianes.negate();
        }

        BigDecimal distanciaAngularFinal = velocidadAngularEnRadianes.multiply(dias);

        BigDecimal coordenadaX = planeta.getRadio().multiply(BigDecimal.valueOf(Math.cos(distanciaAngularFinal.doubleValue())));
        BigDecimal coordenadaY = planeta.getRadio().multiply(BigDecimal.valueOf(Math.sin(distanciaAngularFinal.doubleValue())));

        return new Coordenada(coordenadaX, coordenadaY);
    }

}
