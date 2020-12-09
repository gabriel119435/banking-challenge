package com.model

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "v_curr_balance")
data class Balance(
    @Id val accountId: Int,
    val balance: BigDecimal
)