package com.example.transacciones.validators;

import com.example.transacciones.dto.ResponseMessage;
import org.springframework.stereotype.Component;

@Component
public class TransaccionValidator {

    //Valida que el monto de la trasaccion este correctamente diligenciado
    public ResponseMessage montoValidation(double monto){
        String montoString= String.valueOf(monto);
        try {
            double val=Double.valueOf(montoString);
        } catch (Exception e) {
            return new  ResponseMessage("Valor Ingresado no valido, Solo numeros!",false);
        }

        if (monto<0){
            return new ResponseMessage("No se permiten cantidades negativas",false);
        }

        return new ResponseMessage("todo bien aca",true);
    }
}
