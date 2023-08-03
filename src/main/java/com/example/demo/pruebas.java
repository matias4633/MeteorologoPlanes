package com.example.demo;

import enumerador.Planeta;
import enumerador.SentidoGiro;
import modelo.Coordenada;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class pruebas {
    public static void main(String[] args) {
        for (Long index = 1L ;index <=360 ; index++){
            Coordenada coordenada1 = getCoordenada(Planeta.Ferengi , BigDecimal.valueOf(index));
            Coordenada coordenada2 = getCoordenada(Planeta.Betasoide , BigDecimal.valueOf(index));
            Coordenada coordenada3 = getCoordenada(Planeta.Vulcano , BigDecimal.valueOf(index));
            Coordenada coordenada4 = new Coordenada(BigDecimal.ZERO, BigDecimal.ZERO); //el Sol

            Boolean estanAlineadosEntreSi = verificarAlineacion(coordenada1,coordenada2,coordenada3);
            Boolean solDentroDelTriangulo = elSolEstaDentroDelTriangulo(coordenada1,coordenada2,coordenada3);
            Boolean estanAlineadosConElSOl = false;

            //if(estanAlineadosEntreSi){
                estanAlineadosConElSOl = verificarAlineacion(coordenada1,coordenada2,coordenada4);
            //}
            System.out.println(coordenada1);
            System.out.println(coordenada2);
            System.out.println(coordenada3);
                System.out.println("Entre si ?  : "+estanAlineadosEntreSi);
                System.out.println("Con el sol ?  : "+estanAlineadosConElSOl);
            System.out.println("el sol esta dentro ?  : "+solDentroDelTriangulo);
            System.out.println("-----------");
        }



    }

    private static Boolean verificarAlineacion(Coordenada coordenada1, Coordenada coordenada2, Coordenada coordenada3) {
        final BigDecimal Tolerancia = BigDecimal.valueOf(0.1); // Tolerancia

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
        return pendiente1.subtract(pendiente2).abs().compareTo(Tolerancia) == -1;
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
