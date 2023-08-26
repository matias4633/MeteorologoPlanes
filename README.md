# Sistema de Pronóstico Climático - README

Este repositorio contiene un sistema de pronóstico climático basado en datos de tres civilizaciones en una galaxia lejana: Vulcanos, Ferengis y Betasoides. El sistema está implementado utilizando Java con Spring Boot y MongoDB para la persistencia de datos. Proporciona una API REST que permite obtener pronósticos climáticos para fechas específicas.

## Configuración del Proyecto

### Requisitos Previos
- Java JDK 11 o superior
- Conexión a Internet para acceder a los datos y ejecutar la API

### Pasos de Configuración
1. Clona este repositorio en tu máquina local: `git clone https://github.com/matias4633/MeteorologoPlanetass.git`
2. Tener instalado Docker desktop y abierto.
3. Abrir una consola en la carpeta raiz del proyecto.

## Ejecución del Proyecto (Local)

1. Ejecuta la aplicación con docker mediante el comando: `docker-compose up -d`

No necesita instalar MongoDB, docker ya esta configurado.
## Endpoints (DEPLOY DETENIDO)

La API proporciona los siguientes endpoints:

### 1. Obtener Pronóstico Climático para una Fecha Específica

**Endpoint:** `GET /pronostico`

**Parámetros:**
- `fecha`: Fecha en formato `YYYY-MM-DD`.

**Ejemplo de Uso:**
```http
GET https://meteorologo.fly.dev/pronostico?fecha=2024-02-14 
```


**Respuesta:**
```json
{
    "clima": "CONDICIONES_OPTIMAS",
    "fecha": "2024-02-14"
}
```
### 2. Obtener estadisticas de ultimo proceso de calculo.

**Endpoint:** `GET /estadistica`

**Ejemplo de Uso:**
```http
GET https://meteorologo.fly.dev/pronostico/estadistica
```


**Respuesta:**
```json
{
    "sequia": 21,
    "lluvia": 1189,
    "lluviaMaxima": 39,
    "condicionesOptimas": 2404,
    "fechaActualizacion": "2023-08-05T19:37:15.99"
}
```

### 3. Obtener una lista buscando por clima.

**Endpoint:** `GET /buscar`

**Parámetros:**
- `clima`: `MAXIMA_LLUVIA`.

**OPCIONES**
    SEQUIA, LLUVIA, MAXIMA_LLUVIA , CONDICIONES_OPTIMAS

**Ejemplo de Uso:**
```http
GET https://meteorologo.fly.dev/pronostico/buscar?clima=MAXIMA_LLUVIA
```


**Respuesta:**
```json
[
    {
        "clima": "MAXIMA_LLUVIA",
        "fecha": "2023-10-18"
    },
    {
        "clima": "MAXIMA_LLUVIA",
        "fecha": "2023-11-23"
    }
]
```

**El proceso de calculo se ejecuta automaticamente cada cierto tiempo.**

