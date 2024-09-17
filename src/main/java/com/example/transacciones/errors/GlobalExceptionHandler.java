package com.example.transacciones.errors;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.transacciones.dto.ErrorResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException ex) {
        ErrorResponse responseMessage = new ErrorResponse("Formato de dato inválido: " + ex.getValue(),ex.getLocalizedMessage());
        LOGGER.error("Error: {}",responseMessage.getDetails());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex){
        ErrorResponse responseMessage = new ErrorResponse("Error, Todos los campos tienen que estar diligenciados ",ex.getLocalizedMessage());
        LOGGER.error("Error: {}",responseMessage.getDetails());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        ErrorResponse responseMessage = new ErrorResponse("Error, Formato JSON invalido",ex.getLocalizedMessage());
        LOGGER.error("Error: {}",responseMessage.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<ErrorResponse> handleJWTDecodeException(JWTDecodeException ex){
        ErrorResponse response=new ErrorResponse("Token invalido",ex.getLocalizedMessage());
        LOGGER.error("Error: {}",response.getDetails());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse( "Error", ex.getMessage()); // Mensaje detallado de la excepción
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<ErrorResponse> handleSignatureVerificationException(SignatureVerificationException ex){
        ErrorResponse errorResponse = new ErrorResponse( "Token Invalido", ex.getMessage()); // Mensaje detallado de la excepción
        LOGGER.error("Error: {}",errorResponse.getDetails());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponse> handleJWTVerificationException(JWTVerificationException ex) {
        if (ex instanceof TokenExpiredException) {
            ErrorResponse response=new ErrorResponse("Token expirado",ex.getLocalizedMessage());
            LOGGER.error("Error: {}",response.getDetails());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

        ErrorResponse response=new ErrorResponse("Token invalido",ex.getLocalizedMessage());
        LOGGER.error("Error: {}",response.getDetails());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    //Validacion de existencia de usuario
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex){
        ErrorResponse response=new ErrorResponse("Error",ex.getMessage());
        LOGGER.error("Error: {}",response.getDetails());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    //Validacion de Credenciales
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handlerBadCredentialsException(BadCredentialsException ex){
        ErrorResponse response=new ErrorResponse("Error",ex.getMessage());
        LOGGER.error("Error: {}",response.getDetails());
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }

    //Validacion de Formato JSON incompleto
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String details =ex.getFieldError().toString();
        String message="El formato del JSON no se encuentra completo";
        ErrorResponse errorResponse = new ErrorResponse(message, details);
        LOGGER.error("Error: {}", errorResponse.getDetails());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception){
        String details=exception.getCause().toString();
        String message=exception.getMessage();
        ErrorResponse errorResponse=new ErrorResponse(message,details);
        LOGGER.error("Error{}",details);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
}

