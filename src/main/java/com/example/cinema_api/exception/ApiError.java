package com.example.cinema_api.exception;

import java.time.LocalDateTime;
import java.util.List;

// Esta clase representa el "cuerpo" (body) de la respuesta JSON que recibe el cliente
// cada vez que ocurre un error. Asi todos los errores de la API tienen el mismo formato,
// sin importar en que controller o service haya ocurrido el problema.
public class ApiError {

    private int status;              // codigo HTTP, por ejemplo 404
    private String error;            // nombre del error, por ejemplo "Not Found"
    private String message;          // mensaje explicando que paso, pensado para el cliente
    private LocalDateTime timestamp; // fecha y hora exacta en que ocurrio el error
    private List<String> details;    // lista de detalles extra (solo se usa en errores de validacion)

    // Constructor para errores simples, sin lista de detalles.
    public ApiError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor para errores de validacion, que ademas traen una lista de mensajes
    // (uno por cada campo del request que fallo).
    public ApiError(int status, String error, String message, List<String> details) {
        this(status, error, message); // reutiliza el constructor de arriba
        this.details = details;
    }

    // Getters: Jackson (la libreria que convierte objetos Java a JSON) los necesita
    // para poder armar la respuesta.
    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getDetails() {
        return details;
    }
}
