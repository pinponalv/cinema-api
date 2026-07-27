package com.example.cinema_api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

//TODO: ESTA CLASE DEBO ARREGLARLA PARA PODER UTILIZARLA, AUN NO SE HARA


@RestControllerAdvice
public class GobalExceptionHandler {

//    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
//        return build(HttpStatus.NOT_FOUND, ex.getMessage());
//    }
//
//    @ExceptionHandler(DuplicateResourceException.class)
//    public ResponseEntity<ApiError> handleDuplicate(DuplicateResourceException ex) {
//        return build(HttpStatus.CONFLICT, ex.getMessage());
//    }
//
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {
//        log.warn("Data integrity violation: {}", ex.getMessage());
//        return build(HttpStatus.CONFLICT, "El recurso ya existe o viola una restriccion de datos");
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
//        return build(HttpStatus.UNAUTHORIZED, ex.getMessage());
//    }
//
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity<ApiError> handleUsernameNotFound(UsernameNotFoundException ex) {
//        return build(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
//        return build(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta accion");
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
//        List<String> details = ex.getBindingResult().getFieldErrors().stream()
//                .map(FieldError::getDefaultMessage)
//                .toList();
//        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), "Bad Request",
//                "Error de validacion", details);
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
//        log.error("Unhandled exception", ex);
//        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado");
//    }
//
//    private ResponseEntity<ApiError> build(HttpStatus status, String message) {
//        return ResponseEntity.status(status).body(new ApiError(status.value(), status.getReasonPhrase(), message));
//    }
}
