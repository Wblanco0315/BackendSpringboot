package com.example.transacciones.repositories;

import com.example.transacciones.models.users.TokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<TokenEntity,Long> {

    Optional<TokenEntity> findByToken(String token);
}
