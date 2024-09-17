package com.example.transacciones.repositories;

import com.example.transacciones.models.users.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity,Long> {


    Optional<UserEntity> findUserEntityByCedula(String cedula);
    Optional<UserEntity> findUserEntityByEmail(String email);

    @Query(value = "SELECT * FROM public.users WHERE account_number= :identifier or cedula= :identifier",nativeQuery = true)
    Optional<UserEntity> findByIdentifier(@Param("identifier") String identifier);
}
