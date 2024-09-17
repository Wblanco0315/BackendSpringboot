package com.example.transacciones.repositories;

import com.example.transacciones.models.transactions.TransaccionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransaccionRepository extends CrudRepository<TransaccionEntity,Long> {

    @Query(value = "SELECT t.id,t.monto_transaccion,t.tipo_transaccion,t.fecha_transaccion FROM public.transactions as t INNER JOIN public.transaccion_user as u on id=transaccion_id  WHERE cedula= :cedula ",nativeQuery = true)
    List<TransaccionEntity> findTransaccionesByCedula(@Param("cedula") String cedula);

    @Query(value = "SELECT * FROM public.transactions WHERE user_Id= :userId AND transaction_type=:type",nativeQuery = true)
    List<TransaccionEntity> findAllTransactionsById(@Param("userId") Long userId, @Param("type") String type);
}
