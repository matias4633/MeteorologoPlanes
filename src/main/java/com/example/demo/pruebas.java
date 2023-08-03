package com.example.demo;

import enumerador.Planeta;
import enumerador.SentidoGiro;
import enumerador.TipoClima;
import modelo.Coordenada;
import modelo.Pronostico;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class pruebas {
    private static final BigDecimal TOLERANCIA = BigDecimal.valueOf(0.1); // Tolerancia
    public static void main(String[] args) {
        List<BigDecimal> perimetros = new ArrayList<>();
        Coordenada coordenada4 = new Coordenada(BigDecimal.ZERO, BigDecimal.ZERO); //el Sol
        LocalDate hoy = LocalDate.now();
        List<Pronostico> pronosticos = new ArrayList<>();
        for (Long index = 1L ;index <=360 ; index++){
            Coordenada coordenada1 = getCoordenada(Planeta.Ferengi , BigDecimal.valueOf(index));
            Coordenada coordenada2 = getCoordenada(Planeta.Betasoide , BigDecimal.valueOf(index));
            Coordenada coordenada3 = getCoordenada(Planeta.Vulcano , BigDecimal.valueOf(index));
            Pronostico pronostico = new Pronostico();
            pronostico.setFecha(hoy.plusDays(index));

            Boolean estanAlineadosEntreSi = verificarAlineacion(coordenada1,coordenada2,coordenada3);
            Boolean solDentroDelTriangulo = elSolEstaDentroDelTriangulo(coordenada1,coordenada2,coordenada3);
            Boolean estanAlineadosConElSOl = false;
            BigDecimal perimetro = calcularPerimetro(coordenada1,coordenada2,coordenada3);

            //if(estanAlineadosEntreSi){
                estanAlineadosConElSOl = verificarAlineacion(coordenada1,coordenada2,coordenada4);
            //}

            if(estanAlineadosEntreSi){
                if(estanAlineadosConElSOl){
                    //sequia
                    pronostico.setClima(TipoClima.SEQUIA);
                }else{
                    //condiciones optimas.
                    pronostico.setClima(TipoClima.CONDICIONES_OPTIMAS);
                }
            }else{
                //forman un triangulo.
                if(solDentroDelTriangulo){
                    //lluvia , Opt lluvia maxima.
                    //en este caso me importa el perimetro.
                    perimetros.add(perimetro);
                    pronostico.setClima(TipoClima.LLUVIA);
                }else{
                    //codiciones Optimas , supongo, no lo aclara.
                    pronostico.setClima(TipoClima.CONDICIONES_OPTIMAS);
                }
            }
            pronosticos.add(pronostico);
            System.out.println(coordenada1);
            System.out.println(coordenada2);
            System.out.println(coordenada3);
                System.out.println("Entre si ?  : "+estanAlineadosEntreSi);
                System.out.println("Con el sol ?  : "+estanAlineadosConElSOl);
            System.out.println("el sol esta dentro ?  : "+solDentroDelTriangulo);
            System.out.println("-----------");
        }
        Collections.sort(perimetros , Collections.reverseOrder());
        BigDecimal maximoPerimetro = perimetros.get(0);
        Long diasPico = 0L;
        for (BigDecimal p: perimetros) {
            if(maximoPerimetro.subtract(p).abs().compareTo(TOLERANCIA)==-1){
                diasPico++;
            }else{
                break;
            }
        }

        System.out.println("los dias con pico de lluva son :" +diasPico);

        System.out.println(pronosticos);

    }

    private static BigDecimal calcularPerimetro(Coordenada coordenada1, Coordenada coordenada2, Coordenada coordenada3) {
        BigDecimal lado1 = calcularDistancia(coordenada1, coordenada2);
        BigDecimal lado2 = calcularDistancia(coordenada2, coordenada3);
        BigDecimal lado3 = calcularDistancia(coordenada3, coordenada1);

        BigDecimal perimetro = lado1.add(lado2).add(lado3);
        System.out.println("Perimetro "+perimetro);
        return perimetro;
    }

    public static BigDecimal calcularDistancia(Coordenada p1, Coordenada p2) {
        BigDecimal x1 = p1.getX();
        BigDecimal y1 = p1.getY();
        BigDecimal x2 = p2.getX();
        BigDecimal y2 = p2.getY();

        BigDecimal distanciaX = x2.subtract(x1);
        BigDecimal distanciaY = y2.subtract(y1);

        return distanciaX.pow(2).add(distanciaY.pow(2)).sqrt(MathContext.DECIMAL128);
    }
    private static BigDecimal getPerimetroMaximo() {
        //Por el teorema de la desigualdad triangular
        /* a < (b+c) , b<(a+c) , c<(a+b)
        El valor maximo posible de cada lado es la suma de sus otros dos lados.
        Considerando al triangulo como 3 triangulos pequeños que incluyen al (0,0)
         */
        BigDecimal radio1 = Planeta.Ferengi.getRadio();
        BigDecimal radio2 = Planeta.Vulcano.getRadio();
        BigDecimal radio3 = Planeta.Betasoide.getRadio();
        BigDecimal L = BigDecimal.valueOf(Math.sqrt(radio1.pow(2).add(radio2.pow(2)).add(radio3.pow(2)).doubleValue()));
        BigDecimal perimetroMaximo = L.multiply(BigDecimal.valueOf(Math.sqrt(3.0)));
        return perimetroMaximo;
    }

    private static Boolean verificarAlineacion(Coordenada coordenada1, Coordenada coordenada2, Coordenada coordenada3) {


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

    public static boolean elSolEstaDentroDelTriangulo(Coordenada coordenada1, Coordenada coordenada2, Coordenada coordenada3) {
        double anguloTotal = 0.0;
        anguloTotal += calcularAngulo(coordenada1, coordenada2);
        anguloTotal += calcularAngulo(coordenada2, coordenada3);
        anguloTotal += calcularAngulo(coordenada3, coordenada1);

        // Verificamos si el punto (0,0) está dentro del triángulo comparando la suma de los ángulos con 360 grados
        return Math.abs(anguloTotal - 2 * Math.PI) < 1e-6;
    }

    public static double calcularAngulo(Coordenada p1, Coordenada p2) {
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

    public static Coordenada getCoordenada(Planeta planeta, BigDecimal dias) {
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
