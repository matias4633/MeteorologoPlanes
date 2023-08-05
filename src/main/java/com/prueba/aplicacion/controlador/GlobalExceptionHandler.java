package com.prueba.aplicacion.controlador;

import com.prueba.aplicacion.excepciones.NoEncontradoException;
import com.prueba.aplicacion.excepciones.ParametroInvalidoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParametroInvalidoException.class)
    public ResponseEntity<String> handleParametroInvalidoException(ParametroInvalidoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(NoEncontradoException.class)
    public ResponseEntity<String> handleNoEncontradoException(NoEncontradoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIlegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body("El parametro enviado es invalido");
    }
}