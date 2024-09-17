package com.example.transacciones.repositories;

import com.example.transacciones.models.transactions.DepositEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DepositRepository extends CrudRepository<DepositEntity,Long> {

    @Query(value = "SELECT * FROM public.deposits WHERE cedula= :cedula",nativeQuery = true)
    List<DepositEntity> findAllDepositsByCedula(@Param("cedula") String cedula);
}
