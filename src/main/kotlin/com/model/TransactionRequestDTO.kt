package com.model

import java.math.BigDecimal

data class TransactionRequestDTO(
    val origin: Int?,
    val destination: Int?,
    val amount: BigDecimal,
)