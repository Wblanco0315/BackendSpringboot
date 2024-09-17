package com.example.transacciones.repositories;

import com.example.transacciones.models.users.PermissionEntity;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<PermissionEntity,Long> {
}
