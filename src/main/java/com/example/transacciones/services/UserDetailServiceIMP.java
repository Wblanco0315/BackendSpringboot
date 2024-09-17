package com.example.transacciones.services;

import com.example.transacciones.dto.*;
import com.example.transacciones.dto.sesion.AuthCreateUserRequest;
import com.example.transacciones.dto.sesion.AuthLoginRequest;
import com.example.transacciones.dto.sesion.AuthResponse;
import com.example.transacciones.errors.IllegalArgumentException;
import com.example.transacciones.errors.UsernameNotFoundException;
import com.example.transacciones.models.users.RoleEntity;
import com.example.transacciones.models.enums.RoleEnum;
import com.example.transacciones.models.users.TokenEntity;
import com.example.transacciones.models.users.UserEntity;
import com.example.transacciones.repositories.RoleRepository;
import com.example.transacciones.repositories.TokenRepository;
import com.example.transacciones.repositories.UserRepository;
import com.example.transacciones.util.JWTUtils;
import com.example.transacciones.validators.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.transacciones.errors.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceIMP implements UserDetailsService {
    @Autowired private UserRepository userRepository;

    @Autowired private RoleRepository roleRepository;

    @Autowired private JWTUtils jwtUtils;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private UserValidator validacion;

    @Autowired private TokenRepository tokenRepository;

    static final Logger LOGGER = LogManager.getLogger(UserDetailServiceIMP.class);

    @Override
    //BUSQUEDA DEL USUARIO EN LA BASE DE DATOS
    public UserDetails loadUserByUsername(String email){
        UserEntity userEntity= null;
        try {//Intenta buscar al usuario a travez de la llave primaria, la cedula.
            userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Usuario No encontrado "+email));
        } catch (UsernameNotFoundException e) {//No encontro al usuario, arroja la exection
            throw new RuntimeException(e);
        }
        List<SimpleGrantedAuthority> authorityList=new ArrayList<>(); //Carga la lista de permisos
        try {
            // Busca los roles asignados en el registro
            userEntity.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

            // Busca los permisos de cada rol
            userEntity.getRoles().stream().flatMap(role -> role.getPermissionList().stream()).forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

            //Envia como respuesta al usuario con sus atributos
            return new User(userEntity.getEmail(), userEntity.getPassword(), userEntity.isEnable(), userEntity.isAccountNoExpired(), userEntity.isCredentialNoExpired(), userEntity.isAccountNoLocked(), authorityList);
        } catch (IllegalArgumentException e) {// Atrapa la exception al no encontrar rol/roles asignados
            throw new RuntimeException("Rol/roles no existentes");
        }
    }

    //Inicio de sesion
    public AuthResponse loginUser(AuthLoginRequest authLoginUser){

        String email=authLoginUser.getEmail();
        String password=authLoginUser.getPassword();
        String accessToken="";
        LOGGER.info("Inicio de login para el usuario: {}",email);
        ResponseMessage validation=validacion.logginValidator(password);//Validacion de campos y del usuario
        if (validation.success()){// El usuario fue encontrado
            Authentication authentication=this.authentication(email,password);//Obtiene al usuario autentificado
            accessToken= jwtUtils.createToken(authentication);//Crea el token de autentificacion con los datos del usuario
            SecurityContextHolder.getContext().setAuthentication(authentication); //Crea la sesion autorizando al usuario.
            TokenEntity tokenEntity=new TokenEntity();
            tokenEntity.setToken(accessToken);
            tokenEntity.setLoggedOut(false);
            tokenRepository.save(tokenEntity);
            LOGGER.info("Token Registrado con exito!");
        }
        return new AuthResponse(email, validation.menssage(),accessToken, validation.success());
    }

    //CREACION DEL USUARIO
    public ResponseMessage createUser(AuthCreateUserRequest createRoleRequest) throws IllegalArgumentException{

        List<String>  roles=new ArrayList<>();

        if (userRepository.count()!=0) {
            roles.add(RoleEnum.client.name());
        }else {
            roles.add(RoleEnum.admin.name());
        }
        String name=createRoleRequest.name();
        String email = createRoleRequest.email();
        String password=createRoleRequest.password();
        String cedula=createRoleRequest.cedula();
        LOGGER.info("Inicio de registro para el usuario: {}",email);

        ResponseMessage response=validacion.signUpValidator(name,email,password,cedula);// Valida si el usuario existe
        if (!response.success()){//si el usuario no existe retorna una respuesta
            return response;
        }

        List<String> rolesRequest = roles;//Obtiene la lista de roles asignados en el registro
        Set<RoleEntity> roleEntityList;//Inicializacion de lista de roles
        try {//Prueba si la lista de roles existe
            roleEntityList = roleRepository.findRoleEntitiesByRoleEnumIn(rolesRequest).stream().collect(Collectors.toSet());//guarda los roles del usuario en una lista
        } catch (IllegalArgumentException ex) {//Uno de los roles o rol asignado en el request no exite o no lo encontro
            LOGGER.info("Rol no existente {}",ex.getMessage());
            throw new RuntimeException("Rol/Roles invalidos ");
        }
        if (roleEntityList.isEmpty()) {throw new IllegalArgumentException("The roles specified does not exist."); } //Verifica si se asignaron roles

        //Crea una entidad usuario con sus datos y roles obtenidos anteriormente

        UserEntity userEntity = UserEntity.builder()
                .name(name)
                .email(email)
                .cedula(cedula)
                .password(passwordEncoder.encode(password))
                .accountNumber(generateAccountNumber())
                .balance(new BigDecimal(1000000))
                .roles(roleEntityList)
                .isEnable(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true).build();
        UserEntity userSaved = userRepository.save(userEntity);//Guarda en la base de datos al usuario
        LOGGER.info("Usuario {} creado exitosamente.",cedula);
        return response;
    }

    //Autenticacion
    public Authentication authentication(String email,String password){
        UserDetails userDetails=this.loadUserByUsername(email);//Busca al usuario en la base de datos por email
        if(userDetails==null){//Si la cedula no esta registrada genera una exception
            throw new BadCredentialsException("El usuario "+email+ "no registrado");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())){//si las contraseñas no coincides arroja la exception
            throw new BadCredentialsException("Password incorrecto");
        };
        //Genera la autorizacion
        return new UsernamePasswordAuthenticationToken(email,userDetails.getPassword(),userDetails.getAuthorities());
    }

    //Creacion numero de cuenta
    private String generateAccountNumber() {
        final int DIGITS = 12;
        final Random RANDOM = new Random();
        // Genera un número aleatorio en el rango de 10^11 a 10^12 - 1
        long min = (long) Math.pow(10, DIGITS - 1);
        long max = (long) Math.pow(10, DIGITS) - 1;
        long number = min + (long) (RANDOM.nextDouble() * (max - min));
        LOGGER.info("Numero de cuenta creado con exito");
        return String.valueOf(number);
    }

    //Cerrar sesion
    public ResponseMessage logout(String bearerToken) {
        String jwt=bearerToken.substring(7);
        Optional<TokenEntity> foundedToken=tokenRepository.findByToken(jwt);

        if(!foundedToken.isPresent()){
            TokenEntity token=new TokenEntity();
            token.setToken(jwt);
            token.setLoggedOut(true);
            tokenRepository.save(token);
            return new ResponseMessage("sesion cerrada con exito",true);
        }

        TokenEntity token=foundedToken.get();
        token.setLoggedOut(true);
        SecurityContextHolder.clearContext();
        tokenRepository.save(token);
        return new ResponseMessage("sesion cerrada con exito",true);
    }

}
