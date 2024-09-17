package com.example.transacciones.controllers;

import com.example.transacciones.dto.transactions.DepositoResponse;
import com.example.transacciones.dto.transactions.DepositoRequest;
import com.example.transacciones.dto.ResponseMessage;
import com.example.transacciones.dto.transactions.TransferRequest;
import com.example.transacciones.models.transactions.TransaccionEntity;
import com.example.transacciones.models.users.UserEntity;
import com.example.transacciones.repositories.TransaccionRepository;
import com.example.transacciones.services.TransaccionService;
import com.example.transacciones.services.UserDetailServiceIMP;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/transaccion")
@Slf4j
public class TransaccionController {
    static final Logger LOGGER = LogManager.getLogger(TransaccionController.class);
    @Autowired
    TransaccionService transaccionService;
    @Autowired
    UserDetailServiceIMP userDetailServiceIMP;
    @Autowired
    TransaccionRepository transaccionRepository;

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> consultarSaldo(@RequestHeader("authorization") String token){
        LOGGER.info("Consultando saldo");
        ResponseEntity response=transaccionService.consultarSaldo(token);
        LOGGER.info("Saldo consultado con exito");
        return response;
    }

    @PostMapping("/deposit")
    public ResponseEntity<DepositoResponse> depositar(@RequestHeader("authorization") String token, @RequestBody DepositoRequest request){
        LOGGER.info("inicio de deposito saldo");
        ResponseEntity<DepositoResponse> deposito=transaccionService.depositarMonto(token,request);
        LOGGER.info("Despositado con exito");

        return deposito;
    }

    @PostMapping ("/withdrawal")
    public ResponseEntity<DepositoResponse> retiro(@RequestHeader("authorization")String token, @RequestBody DepositoRequest request){

        LOGGER.info("inicio de retiro");
        ResponseEntity<DepositoResponse> retiro=transaccionService.retirarMonto(request,token);
        LOGGER.info("Retirado con exito");
        return retiro;
    }

    @PostMapping("/transfer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DepositoResponse> transferencia(@RequestHeader("authorization") String token, @RequestBody TransferRequest request){
       LOGGER.info("inicio de retiro");
       ResponseEntity<DepositoResponse>transferencia=transaccionService.transferencia(token,request);
       LOGGER.info("Transferido con exito");
      return transferencia;
   }
}
