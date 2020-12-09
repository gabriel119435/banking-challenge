package com.repository

import com.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepo : JpaRepository<Account, Int> {
    fun findByCpf(cpf: String): Account?
}