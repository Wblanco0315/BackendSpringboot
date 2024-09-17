package com.example.transacciones.repositories;

import com.example.transacciones.models.transactions.WithdrawalEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface WithdrawalRepository extends CrudRepository<WithdrawalEntity,Long> {

    @Query(value = "SELECT * FROM public.withdrawals WHERE cedula= :userId",nativeQuery = true)
    List<WithdrawalEntity> findAllTransactionsById(@Param("userId") String userId);
}
