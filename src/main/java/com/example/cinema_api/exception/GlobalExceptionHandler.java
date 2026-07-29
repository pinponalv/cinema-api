package com.example.cinema_api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

// @RestControllerAdvice hace que esta clase intercepte las excepciones lanzadas
// por CUALQUIER controller de la app. Sin esta clase, cada excepcion sin manejar
// termina en un error 500 generico de Spring, sin un formato claro para el cliente.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // El logger sirve para dejar registro en la consola/archivo de log de los errores
    // que ocurren en el servidor, sin necesidad de mostrarselos al cliente.
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Se lanza cuando buscamos algo por id (usuario, rol, permiso, pelicula) y no existe.
    // Respondemos con 404 Not Found.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Se lanza cuando se intenta crear un usuario con un email o username que ya existe.
    // Respondemos con 409 Conflict, porque el recurso ya existe.
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicate(DuplicateResourceException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Se lanza a mano en los services cuando el request esta incompleto o mal formado
    // de una manera que las anotaciones de validacion (@NotBlank, @Size, etc) no detectan
    // (por ejemplo, un set de roles vacio). Respondemos con 400 Bad Request.
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiError> handleInvalidRequest(InvalidRequestException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Esta excepcion la lanza la base de datos (a traves de JPA/Hibernate) cuando se viola
    // una restriccion, por ejemplo un "unique" en username o email. Es una red de seguridad:
    // aunque ya validamos antes en el service, esto cubre el caso de dos requests simultaneos
    // (condicion de carrera) que pasan la validacion al mismo tiempo.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Violacion de integridad de datos: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, "El recurso ya existe o viola una restriccion de datos");
    }

    // Spring lanza esta excepcion automaticamente cuando un @RequestBody anotado con @Valid
    // no cumple alguna de las validaciones definidas en el DTO (@NotBlank, @Email, @Size, etc).
    // Armamos una lista con un mensaje por cada campo que fallo, usando un for comun
    // (sin streams) para que sea facil de leer paso a paso.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<String> details = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            // Cada detalle queda como "campo: mensaje", por ejemplo "email: Debe tener un formato valido"
            String detalle = fieldError.getField() + ": " + fieldError.getDefaultMessage();
            details.add(detalle);
        }

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Error de validacion",
                details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Se lanza cuando el login falla porque el usuario o la contrasena no coinciden.
    // Respondemos 401 Unauthorized sin aclarar cual de los dos datos esta mal,
    // para no darle pistas a quien intenta adivinar credenciales.
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
    }

    // Se lanza cuando un usuario SI esta autenticado (tiene un token valido) pero
    // intenta acceder a un endpoint para el que no tiene el rol necesario
    // (por ejemplo, un USER llamando un endpoint que exige ADMIN).
    // Respondemos con 403 Forbidden.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "No tenes permisos para realizar esta accion");
    }

    // Cualquier excepcion que no hayamos previsto en los handlers de arriba cae aca
    // como ultimo recurso. Nunca devolvemos el mensaje interno real al cliente
    // (podria exponer detalles internos), pero si lo dejamos registrado en el log
    // para poder investigarlo despues.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        log.error("Excepcion sin manejar", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado");
    }

    // Metodo auxiliar privado para no repetir la misma construccion de respuesta
    // en cada handler de arriba (todos arman un ApiError y lo devuelven con un status).
    private ResponseEntity<ApiError> build(HttpStatus status, String message) {
        ApiError error = new ApiError(status.value(), status.getReasonPhrase(), message);
        return ResponseEntity.status(status).body(error);
    }
}
