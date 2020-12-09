package com.model

import com.util.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransferResponseDTO(
    val type: TransactionType,
    val destination: Int,
    val amount: BigDecimal,
    val createdAt: LocalDateTime
)

data class DepositWithdrawResponseDTO(
    val type: TransactionType,
    val amount: BigDecimal,
    val createdAt: LocalDateTime
)