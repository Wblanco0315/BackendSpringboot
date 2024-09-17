package com.example.transacciones.controllers;


import com.example.transacciones.models.transactions.AuditEntity;
import com.example.transacciones.models.users.UserEntity;
import com.example.transacciones.repositories.AuditRepository;
import com.example.transacciones.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AuditController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuditRepository auditRepository;

    @GetMapping("/users")
    public ResponseEntity<Iterable<UserEntity>> getUsers(){
        Iterable<UserEntity> usuarios= userRepository.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/audits")
    public ResponseEntity<Iterable<AuditEntity>> getAudits(){
        Iterable<AuditEntity> auditorias=auditRepository.findAll();
        return new  ResponseEntity<>(auditorias,HttpStatus.OK);
    }

}
