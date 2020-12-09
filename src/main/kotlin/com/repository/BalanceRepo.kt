package com.repository

import com.model.Balance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BalanceRepo : JpaRepository<Balance, Int>