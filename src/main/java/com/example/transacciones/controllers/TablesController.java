package com.example.transacciones.controllers;

import com.example.transacciones.models.enums.TransactionType;
import com.example.transacciones.models.transactions.DepositEntity;
import com.example.transacciones.models.transactions.TransaccionEntity;
import com.example.transacciones.models.transactions.WithdrawalEntity;
import com.example.transacciones.models.users.UserEntity;
import com.example.transacciones.repositories.DepositRepository;
import com.example.transacciones.repositories.TransaccionRepository;
import com.example.transacciones.repositories.WithdrawalRepository;
import com.example.transacciones.services.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tables")
public class TablesController {

    @Autowired
    DepositRepository depositRepository;

    @Autowired
    TransaccionService transaccionService;

    @Autowired
    TransaccionRepository transaccionRepository;

    @Autowired
    WithdrawalRepository withdrawalRepository;

    @GetMapping("/deposits")

    public ResponseEntity<Iterable<DepositEntity>>getUserDeposits(@RequestHeader("Authorization") String bearerToken){
        Optional<UserEntity> usuario=transaccionService.buscarUsuarioLoggeado(bearerToken);

        if (!usuario.isPresent()){
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        String cedula=usuario.get().getCedula();

        Iterable<DepositEntity> depositos=depositRepository.findAllDepositsByCedula(cedula);
        return new ResponseEntity(depositos, HttpStatus.OK);
    }

    @GetMapping("/transactions")

    public ResponseEntity<List<TransaccionEntity>>getUserTransactions(@RequestHeader("Authorization") String bearerToken){

        Optional<UserEntity> usuarioBuscado=transaccionService.buscarUsuarioLoggeado(bearerToken);
        if (!usuarioBuscado.isPresent()){
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        UserEntity usuario=usuarioBuscado.get();

        List<TransaccionEntity> transacciones=transaccionRepository.findAllTransactionsById(usuario.getId(), TransactionType.TRANSFER.name());
        return new  ResponseEntity(transacciones,HttpStatus.OK);
    }

    @GetMapping("/withdrawals")
    public ResponseEntity<List<WithdrawalEntity>> withdrawals(@RequestHeader("Authorization") String bearerToken) {
        Optional<UserEntity> usuarioBuscado = transaccionService.buscarUsuarioLoggeado(bearerToken);
        if(!usuarioBuscado.isPresent()){
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        UserEntity usuario = usuarioBuscado.get();
        List<WithdrawalEntity> retiros = withdrawalRepository.findAllTransactionsById(usuario.getCedula());
        return new  ResponseEntity<>(retiros, HttpStatus.OK);
    }


}
