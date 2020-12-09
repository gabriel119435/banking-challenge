package com.controller

import com.model.TransactionRequestDTO
import com.service.TransactionService
import com.util.ERROR
import com.util.TRANSACTION_URL
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(TRANSACTION_URL)
class TransactionController(val transactionService: TransactionService) {

    @PostMapping()
    fun save(@RequestBody request: TransactionRequestDTO): ResponseEntity<Any> {
        val error = transactionService.validateAndExecuteTransaction(request)
        return if (error.isPresent) ResponseEntity(mapOf(ERROR to error.get()), BAD_REQUEST)
        else ResponseEntity(CREATED)
    }
}