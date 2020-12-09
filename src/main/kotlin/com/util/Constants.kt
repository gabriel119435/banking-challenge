package com.util

enum class TransactionType {
    DEPOSIT, WITHDRAW, TRANSFER
}

//url
const val CPF_URL = "/{cpf}"
const val PEOPLE_URL = "/people"
const val TRANSACTION_URL = "/transaction"
const val HISTORY_URL = "/history"
const val ID_URL = "/{id}"

const val ERROR = "error"

// message errors
const val INVALID_CPF = "invalid cpf"
const val CPF_ALREADY_REGISTERED = "cpf already registered"
const val BALANCE_NOT_SETTLED = "balance not settled"
const val ORIGIN_DEST_NULL = "origin and destination null"
const val ORIGIN_DOESNT_EXIST = "origin account doesn't exist"
const val DESTINATION_DOESNT_EXIST = "destination account doesn't exist"
const val INSUFFICIENT_FUNDS = "insufficient founds"
const val ACCOUNT_DOESNT_EXIST = "account doesn't exist"


const val DEPOSIT_MULTIPLIER = "1.005"
const val WITHDRAW_MULTIPLIER = "1.01"
const val DEFAULT_MULTIPLIER = "1"

const val ERROR_KEY = "$.error"
const val NAME_KEY = "$.name"
const val CPF_KEY = "$.cpf"
const val ROOT_KEY = "$"
const val BALANCE_BALANCE_KEY = "$.balance.balance"
const val TRANSACTIONS_KEY = "$.transactions"

