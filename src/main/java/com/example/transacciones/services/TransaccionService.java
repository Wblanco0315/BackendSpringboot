package com.example.transacciones.services;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.transacciones.dto.*;
import com.example.transacciones.dto.transactions.DepositoRequest;
import com.example.transacciones.dto.transactions.DepositoResponse;
import com.example.transacciones.dto.transactions.TransferRequest;
import com.example.transacciones.models.enums.TransactionStatus;
import com.example.transacciones.models.enums.TransactionType;
import com.example.transacciones.models.transactions.AuditEntity;
import com.example.transacciones.models.transactions.DepositEntity;
import com.example.transacciones.models.transactions.TransaccionEntity;
import com.example.transacciones.models.transactions.WithdrawalEntity;
import com.example.transacciones.models.users.TokenEntity;
import com.example.transacciones.models.users.UserEntity;
import com.example.transacciones.repositories.*;
import com.example.transacciones.util.JWTUtils;
import com.example.transacciones.validators.TransaccionValidator;
import com.example.transacciones.validators.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TransaccionService{

    @Autowired UserRepository userRepository;

    @Autowired TransaccionRepository transaccionRepository;

    @Autowired TransaccionValidator transaccionValidator;

    @Autowired DepositRepository depositRepository;

    @Autowired UserValidator userValidator;

    @Autowired TokenRepository tokenRepository;

    @Autowired WithdrawalRepository withdrawalRepository;

    @Autowired AuditRepository auditRepository;

    @Autowired JWTUtils jwtUtils;

    @Autowired private PasswordEncoder passwordEncoder;

    static final Logger LOGGER = LogManager.getLogger(TransaccionService.class);

    //Buscar Usuario logeado atravez del token en la base de datos
    public  Optional<UserEntity> buscarUsuarioLoggeado(String bearerToken){
        Optional<UserEntity> user;
        String cedula="";
        String token = bearerToken.substring(7); //obtemenos una subcadena de texto para eliminar "BEARER"
        DecodedJWT decodedJWT;

        try { //Prueba si el token es valido
            decodedJWT = jwtUtils.validateToken(token);
        }
        catch (JWTDecodeException e){ //Toma la excepcion al no ser un token valido
            LOGGER.error("JWT DecodeException {}",e.getLocalizedMessage());
            throw new JWTDecodeException("Token invalido");
        }

        //Extrae la Cedula del token, el cual esta asigando como el subject.
        cedula = decodedJWT.getSubject();

        //Se realiza una busqueda en la base de datos por medio de la cedula
        user = userRepository.findUserEntityByCedula(cedula);
        return user;
    }

    //CONSULTA DE SALDO
    public ResponseEntity<BigDecimal> consultarSaldo(String token){
        if(isLogout(token)){ return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);}
        Optional<UserEntity> usuario=buscarUsuarioLoggeado(token);//Busca al usuario loggeado a traves del JWT
        if (!usuario.isPresent()){//No encontro al usuario, podria deberse a un error con el token o este se encuentra vencido
            LOGGER.warn("Token Invalido");
            return new ResponseEntity(null,HttpStatus.NOT_FOUND);
        }
        BigDecimal balance=usuario.get().getBalance();
        return new ResponseEntity(balance,HttpStatus.OK);
    }

    //DEPOSITAR MONTO
    public ResponseEntity<DepositoResponse> depositarMonto(String bearerToken, DepositoRequest request){

        if(isLogout(bearerToken)){return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);}
        AuditEntity audit;
        DepositoResponse depositoResponse;

        Optional <UserEntity> usuarioBuscado=buscarUsuarioLoggeado(bearerToken);//Busca al usuario
        if (!usuarioBuscado.isPresent()){
            LOGGER.warn("usuario no encontrado");
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

        UserEntity usuario=usuarioBuscado.get();//Se Obtiene la entidad UserEntity
        ResponseMessage montoResponse=transaccionValidator.montoValidation(request.getAmount());//Valida que este correctamente diligenciado
        if (!montoResponse.success()){//El monto no paso las validaciones
            createAudit(usuario,BigDecimal.valueOf(request.getAmount()),TransactionType.DEPOSIT,TransactionStatus.FAILED,null);
            LOGGER.warn(montoResponse.menssage());
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

        BigDecimal monto= BigDecimal.valueOf(request.getAmount());//Convierte a BigDecimal
        BigDecimal balance= usuario.getBalance();//Obtenemos el saldo del usuario al que se le deposita
        BigDecimal total=balance.add(monto);//suma los valores
        usuario.setBalance(total);//Se guarda el nuevo saldo
        userRepository.save(usuario);//Se guardan los cambios en la Base de datos

        DepositEntity deposito= DepositEntity.builder().name(usuario.getName()).amount(BigDecimal.valueOf(request.getAmount())).cedula(usuario.getCedula()).accountNumber(usuario.getAccountNumber()).build();
        depositRepository.save(deposito);//se guarda la trasaccion en base de datos


        //Genera la transaccion
        createTransaccion(usuario,BigDecimal.valueOf(request.getAmount()),TransactionType.DEPOSIT,TransactionStatus.COMPLETED,null);
        createAudit(usuario,monto,TransactionType.DEPOSIT,TransactionStatus.COMPLETED,null);
        depositoResponse=new DepositoResponse("Depósito completado exitosamente");

        return new  ResponseEntity<>(depositoResponse,HttpStatus.CREATED);
    }

    // RETIRAR MONTO
    public ResponseEntity<DepositoResponse> retirarMonto(DepositoRequest request,String bearerToken){

        if(isLogout(bearerToken)){ return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);}

        String token=bearerToken.substring(7);
        Optional <TokenEntity> foundToken=tokenRepository.findByToken(token);
        DepositoResponse depositoResponse;

        Optional <UserEntity> usuarioBuscado=buscarUsuarioLoggeado(bearerToken);//Busca al usuario

        if (!usuarioBuscado.isPresent()){//Si el usuario no exite
            String message= "Usuario con cedula "+request.getIdentifier()+" no encontrado!";
            LOGGER.warn(message);
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

        UserEntity usuario=usuarioBuscado.get();//Se Obtiene la entidad UserEntity
        ResponseMessage montoResponse=transaccionValidator.montoValidation(request.getAmount());//Valida que este correctamente diligenciado
        if (!montoResponse.success()){//El monto no paso las validaciones
            createAudit(usuario,BigDecimal.valueOf(request.getAmount()),TransactionType.WITHDRAWAL,TransactionStatus.FAILED,null);
            LOGGER.warn(montoResponse.menssage());
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

        BigDecimal monto= BigDecimal.valueOf(request.getAmount());//Convierte a BigDecimal

        BigDecimal balance= usuario.getBalance();//Obtenemos el saldo del usuario al que se le deposita
        BigDecimal total=balance.subtract(monto);//resta los valores
        usuario.setBalance(total);//Se guarda el nuevo saldo
        userRepository.save(usuario);//Se guardan los cambios en la Base de datos

        WithdrawalEntity retiro= WithdrawalEntity.builder().userId(usuario.getId()).amount(BigDecimal.valueOf(request.getAmount())).cedula(usuario.getCedula()).accountNumber(usuario.getAccountNumber()).status("completed").build();
        withdrawalRepository.save(retiro);//se guarda la trasaccion en base de datos
        createTransaccion(usuario,BigDecimal.valueOf(request.getAmount()),TransactionType.WITHDRAWAL,TransactionStatus.COMPLETED,null);
        createAudit(usuario,BigDecimal.valueOf(request.getAmount()),TransactionType.WITHDRAWAL,TransactionStatus.COMPLETED,null);
        depositoResponse=new DepositoResponse("Depósito completado exitosamente");

        return new ResponseEntity<>(depositoResponse,HttpStatus.CREATED);
    }

    // Transferencias entre usuarios
    public ResponseEntity<DepositoResponse> transferencia(String bearerToken, TransferRequest request){

        if(isLogout(bearerToken)){ return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);}

        String token=bearerToken.substring(7);
        Optional <TokenEntity> foundToken=tokenRepository.findByToken(token);
        DepositoResponse depositoResponse;

        Optional <UserEntity> usuarioOrigen=buscarUsuarioLoggeado(bearerToken);//Busca al usuario
        if (!usuarioOrigen.isPresent()){//Si el usuario no exite
            String message= "Usuario con cedula o AccountNumber "+request.identifierOrigen()+" no encontrado!";
            LOGGER.warn(message);
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        UserEntity usuario=usuarioOrigen.get();//Se Obtiene la entidad UserEntity

        Optional<UserEntity> buscarDestino=userRepository.findByIdentifier(request.identifierDestino());
        if (!buscarDestino.isPresent()){
            String message= "Usuario con cedula "+request.identifierDestino()+" no encontrado!";
            createAudit(usuario,BigDecimal.valueOf(request.amount()),TransactionType.TRANSFER,TransactionStatus.FAILED,null);
            LOGGER.warn(message);
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

        UserEntity usuarioDestino=buscarDestino.get();

        ResponseMessage montoResponse=transaccionValidator.montoValidation(request.amount());//Valida que este correctamente diligenciado
        if (!montoResponse.success()){//El monto no paso las validaciones
            createAudit(usuario,BigDecimal.valueOf(request.amount()),TransactionType.TRANSFER,TransactionStatus.FAILED,null);
            LOGGER.warn(montoResponse.menssage());
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

        BigDecimal monto= BigDecimal.valueOf(request.amount());//Convierte a BigDecimal

        BigDecimal balance= usuario.getBalance();//Obtenemos el saldo del usuario al que se le deposita
        BigDecimal total=balance.subtract(monto);//resta los valores
        usuario.setBalance(total);//Se guarda el nuevo saldo
        userRepository.save(usuario);//Se guardan los cambios en la Base de datos

        BigDecimal balanceDestino=usuarioDestino.getBalance();
        usuarioDestino.setBalance(balanceDestino.add(monto));
        userRepository.save(usuarioDestino);

        createTransaccion(usuario,BigDecimal.valueOf(request.amount()),TransactionType.TRANSFER,TransactionStatus.COMPLETED,usuarioDestino.getAccountNumber());
        createAudit(usuario,BigDecimal.valueOf(request.amount()),TransactionType.TRANSFER,TransactionStatus.COMPLETED,usuarioDestino.getAccountNumber());
        depositoResponse=new DepositoResponse("Transferencia completada exitosamente");
        return new ResponseEntity<>(depositoResponse,HttpStatus.CREATED);
    }

    //Creador de Transacciones
    public void createTransaccion(UserEntity usuario,BigDecimal amount,TransactionType type,TransactionStatus status,String destination){
        TransaccionEntity transaccion=TransaccionEntity.builder()
                .userId(usuario.getId())
                .name(usuario.getName())
                .cedula(usuario.getCedula())
                .accountNumber(usuario.getAccountNumber())
                .amount(amount)
                .transactionType(type)
                .status(status)
                .destinationAccount(destination)
                .build();
        LOGGER.info("Transasccion generada, status{}",status);
        transaccionRepository.save(transaccion);
    }

    public void createAudit(UserEntity usuario,BigDecimal amount,TransactionType type,TransactionStatus status,String destino){
        AuditEntity audit=AuditEntity.builder()
                .userId(usuario.getId())
                .cedula(usuario.getCedula())
                .userName(usuario.getName())
                .amount(amount)
                .transactionStatus(status)
                .transactionType(type)
                .destinationAccount(destino)
                .accountNumber(usuario.getAccountNumber())
                .build();
        auditRepository.save(audit);
    }

    public boolean isLogout(String bearerToken){
        String token=bearerToken.substring(7);
        Optional <TokenEntity> foundToken=tokenRepository.findByToken(token);
        if (!foundToken.isPresent()){
            TokenEntity newToken=new TokenEntity();
            newToken.setToken(token);
            newToken.setLoggedOut(true);
            tokenRepository.save(newToken);
        }

        if (foundToken.get().isLoggedOut()){//token vencido o no valido
            return true;
        }
        LOGGER.error("Error de Autentificacion, token invalido o vencido");

        return false;
    }
}
