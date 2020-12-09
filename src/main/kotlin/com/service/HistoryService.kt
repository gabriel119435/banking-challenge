package com.service

import com.model.DepositWithdrawResponseDTO
import com.model.HistoryDTO
import com.model.TransferResponseDTO
import com.repository.BalanceRepo
import com.repository.TransactionRepo
import com.util.TransactionType
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class HistoryService(
    val accountService: AccountService,
    val transactionService: TransactionService,
    val balanceRepo: BalanceRepo,
    val transactionRepo: TransactionRepo
) {
    fun findById(id: Int, daysOp: Optional<Int>): Any {
        accountService.validateAccount(id).also { error -> if (error.isPresent) return error.get() }
        val balance = balanceRepo.findById(id).get()
        if (!daysOp.isPresent) return balance
        val transactions = transactionRepo.findAllTransactionsRelatedToThisAccount(
            id,
            LocalDate.now().minusDays(daysOp.get().toLong())
        ).map { t ->
            transactionService.recognizeType(t.origin, t.destination).get().let { type ->
                if (TransactionType.TRANSFER == type) {
                    TransferResponseDTO(
                        type,
                        t.destination!!,
                        t.amount,
                        t.createdAt
                    )
                } else
                    DepositWithdrawResponseDTO(
                        type,
                        t.amount,
                        t.createdAt
                    )
            }
        }
        return HistoryDTO(balance, transactions)

    }

}
