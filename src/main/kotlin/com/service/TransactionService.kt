package com.service

import com.model.Transaction
import com.model.TransactionRequestDTO
import com.repository.TransactionRepo
import com.util.*
import com.util.TransactionType.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
class TransactionService(
    val accountService: AccountService,
    val transactionRepo: TransactionRepo
) {
    // only message error is returned, in a successfully case this will return empty
    fun validateAndExecuteTransaction(request: TransactionRequestDTO): Optional<String> {
        val typeOp = recognizeType(request.origin, request.destination)
        if (!typeOp.isPresent) return Optional.of(ORIGIN_DEST_NULL)
        val accountValidationError =
            accountService.validateAccounts(request.origin, request.destination)
        if (accountValidationError.isPresent) return accountValidationError

        val multipliedAmount = request.amount.multiply(BigDecimal(getMultiplier(typeOp.get())))
        if (DEPOSIT != typeOp.get()) {
            val withdrawAmountValidationError =
                accountService.validateWithdraw(request.origin!!, multipliedAmount)
            if (withdrawAmountValidationError.isPresent) return withdrawAmountValidationError
        }
        transactionRepo.save(
            Transaction(
                origin = request.origin,
                destination = request.destination,
                amount = multipliedAmount,
                createdAt = LocalDateTime.now()
            )
        )
        return Optional.empty()
    }

    private fun getMultiplier(type: TransactionType) =
        when (type) {
            DEPOSIT -> DEPOSIT_MULTIPLIER
            WITHDRAW -> WITHDRAW_MULTIPLIER
            else -> DEFAULT_MULTIPLIER
        }

    fun recognizeType(origin: Int?, destination: Int?): Optional<TransactionType> =
        if (origin != null) // some account will send money
            if (destination != null) Optional.of(TRANSFER) // some account will receive
            else Optional.of(WITHDRAW)
        else
            if (destination != null) Optional.of(DEPOSIT)
            else Optional.empty() // bad transaction
}