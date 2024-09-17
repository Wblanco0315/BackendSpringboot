package com.example.transacciones.errors;


public class SignatureVerificationException extends RuntimeException{
    public SignatureVerificationException(String message) {
        super(message);
    }
}
