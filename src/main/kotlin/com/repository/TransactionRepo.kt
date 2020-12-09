package com.repository

import com.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TransactionRepo : JpaRepository<Transaction, Int> {

    @Query(
        value = "select * from transaction where (origin = :id or destination = :id) and created_at > :after order by created_at",
        nativeQuery = true
    )
    fun findAllTransactionsRelatedToThisAccount(
        @Param("id") id: Int,
        @Param("after") after: LocalDate
    ): List<Transaction>
}