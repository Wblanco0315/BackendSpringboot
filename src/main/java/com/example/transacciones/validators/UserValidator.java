package com.example.transacciones.validators;

import com.example.transacciones.dto.ResponseMessage;
import com.example.transacciones.models.users.UserEntity;
import com.example.transacciones.repositories.UserRepository;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Getter
@Component
public class UserValidator {

    @Autowired
    private static final Logger LOGGER = LogManager.getLogger(UserValidator.class);
    @Autowired
    private UserRepository userRepository;

    private String message;

    //Valida que los campos esten correctamente diligenciados
    public ResponseMessage logginValidator(String password){
        if (password.length()<8){
            message="La contraseña debe tener minimo 8 caracteres";
            LOGGER.warn("warn {}",message);
            return new ResponseMessage(message,false);
        }
        return new ResponseMessage("User Loggin Sussesfully",true);
    }

    //Valida que el usuario registrado no exista
    public ResponseMessage signUpValidator(String name,String email,String password, String cedula){
        ResponseMessage response=logginValidator(password);
        Optional<UserEntity> user=userRepository.findUserEntityByEmail(email);
        if (user.isPresent()){
            message="El usuario ya existe";
            LOGGER.warn("warn {}",message);
            response=new ResponseMessage(message,false);
        }

        if (name.length()<3){
            message="nombre muy corto";
            LOGGER.warn("warn {}",message);
            response=new ResponseMessage(message,false);
        }

        if (cedula.length()<5){
            message="cedula muy corta, invalida";
            LOGGER.warn("warn{}",message);
            response=new ResponseMessage(message,false);
        }

        if (response.success()){
            LOGGER.info("Usuario {} creado exitosamente.",email);
            return new ResponseMessage("Registrado con exito",true);
        }
        return response;
    }

}
