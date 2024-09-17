package com.example.transacciones.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.transacciones.models.users.RoleEntity;
import com.example.transacciones.models.users.UserEntity;
import com.example.transacciones.repositories.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Getter
public class JWTUtils {

    //Clave privada
    @Value("${security.jwt.key-private}")
    private String privateKey;

    //Nombre de usuario que genera el token
    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    @Autowired
    UserRepository userRepository;

    public String createToken(Authentication authentication){

        Algorithm algorithm=Algorithm.HMAC256(this.privateKey);//Algoritmo de generacion del token con la clave privada
        String email=authentication.getPrincipal().toString();
        Optional<UserEntity> userFound=userRepository.findUserEntityByEmail(email);
        UserEntity user=userFound.get();

        String authorities=authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        Set<RoleEntity> rol=user.getRoles();

        ArrayList<RoleEntity> list = new ArrayList<>(rol);
        String roleName=list.get(0).getRoleEnum().name();

        Map<String,Object> payload=new  HashMap<>();
        payload.put("cedula",user.getCedula());
        payload.put("name",user.getName());
        payload.put("role",roleName);
        payload.put("id",user.getId());
        payload.put("email",user.getEmail());
        payload.put("accountNumber",user.getAccountNumber());
        payload.put("balance",user.getBalance().toString());

        String token= JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(user.getCedula())
                .withPayload(payload)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+86400000))//Vencimiento en ms, 1H.
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))//Tiempo de inicio
                .sign(algorithm);//Algoritmo de encriptacion a utilazar
        return token;

    }

    //Desencriptacion del JWT
    public DecodedJWT validateToken(String token){
        DecodedJWT decodedJWT; //Decodificador del token

        Algorithm algorithm = Algorithm.HMAC256(this.privateKey); //algoritmo de encriptacion
        JWTVerifier verifier = JWT.require(algorithm)//Se pasa el mismo algoritmo que se uso al encriptar
                .withIssuer(this.userGenerator)//Se manda el usuario que genero el token
                .build();
        decodedJWT = verifier.verify(token);//se verifica que el token es valido
        return decodedJWT; //Devuelve el token ya desencriptado
    }
    public String extractCedula(DecodedJWT decodedJWT)throws JWTDecodeException {
        return decodedJWT.getSubject().toString();//Obtenemos el subject que en este caso es la cedula
    }
    //se obtienen el claim(atributos que contienen el token) especificado
    public Claim getSpecificClaim(DecodedJWT decodedJWT,String claimName){
        return decodedJWT.getClaim(claimName);
    }

}
