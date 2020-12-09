package com.service

import com.repository.AccountRepo
import com.repository.BalanceRepo
import com.util.ACCOUNT_DOESNT_EXIST
import com.util.DESTINATION_DOESNT_EXIST
import com.util.INSUFFICIENT_FUNDS
import com.util.ORIGIN_DOESNT_EXIST
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class AccountService(
    val balanceRepo: BalanceRepo,
    val accountRepo: AccountRepo,
) {

    fun validateAccounts(origin: Int?, destination: Int?): Optional<String> {
        origin?.let {
            if (!accountRepo.findById(it).isPresent)
                return Optional.of(ORIGIN_DOESNT_EXIST)
        }
        destination?.let {
            if (!accountRepo.findById(it).isPresent)
                return Optional.of(DESTINATION_DOESNT_EXIST)
        }
        return Optional.empty()
    }

    fun validateAccount(id: Int): Optional<String> =
        if (!accountRepo.findById(id).isPresent) Optional.of(ACCOUNT_DOESNT_EXIST)
        else Optional.empty()

    fun validateWithdraw(id: Int, amount: BigDecimal): Optional<String> {
        val currBalance = balanceRepo.findById(id).get()
        return if (currBalance.balance >= amount) Optional.empty()
        else Optional.of(INSUFFICIENT_FUNDS)
    }

}
