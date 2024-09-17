package com.example.transacciones.repositories;

import com.example.transacciones.models.transactions.AuditEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuditRepository extends CrudRepository<AuditEntity,Long> {

}
