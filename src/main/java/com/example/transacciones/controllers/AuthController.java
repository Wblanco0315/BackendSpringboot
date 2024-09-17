package com.example.transacciones.controllers;

import com.example.transacciones.dto.sesion.AuthCreateUserRequest;
import com.example.transacciones.dto.sesion.AuthLoginRequest;
import com.example.transacciones.dto.sesion.AuthResponse;
import com.example.transacciones.dto.ResponseMessage;
import com.example.transacciones.errors.IllegalArgumentException;
import com.example.transacciones.services.UserDetailServiceIMP;
import com.example.transacciones.util.JWTUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@Slf4j
@Controller
public class AuthController {

    static final Logger LOGGER = LogManager.getLogger(AuthController.class);
    @Autowired
    private UserDetailServiceIMP userDetailService;

    @Autowired
    JWTUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest userRequest){
        AuthResponse response=this.userDetailService.loginUser(userRequest);
        if(!response.status()){
            LOGGER.error("Error al iniciar sesion con usuario: {}",response.message());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        LOGGER.info("inicio de sesion exitoso, Usuario {} .",userRequest.getEmail());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@RequestBody @Valid AuthCreateUserRequest userRequest)throws MethodArgumentNotValidException, IllegalArgumentException{
        ResponseMessage response=this.userDetailService.createUser(userRequest);
        if (!response.success()){
            LOGGER.error("Error al crear el usuario: {}",userRequest.cedula());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage> loggout(@RequestHeader("authorization") String token){
        ResponseMessage response=userDetailService.logout(token);
        LOGGER.info("Cierrre de sesion con exito!");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
