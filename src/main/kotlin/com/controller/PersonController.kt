package com.controller

import com.model.Person
import com.service.PersonService
import com.util.*
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(PEOPLE_URL)
class PersonController(val personService: PersonService) {

    @GetMapping
    fun findAll() = personService.getAll()

    @GetMapping(CPF_URL)
    fun findByCpf(@PathVariable cpf: String) =
        if (cpf.isValidCpf())
            personService.findByCpf(cpf)
                ?.let { ResponseEntity(it, OK) }
                ?: run { ResponseEntity(NO_CONTENT) }
        else ResponseEntity(mapOf(ERROR to INVALID_CPF), BAD_REQUEST)

    @PostMapping
    fun save(@RequestBody body: Person) =
        if (body.cpf.isValidCpf())
            if (personService.findByCpf(body.cpf) != null)
            // since idempotency would result in overwriting, cpfs already registered result in 404 http status
                ResponseEntity(mapOf(ERROR to CPF_ALREADY_REGISTERED), BAD_REQUEST)
            else ResponseEntity(personService.save(body), CREATED)
        else
            ResponseEntity(mapOf(ERROR to INVALID_CPF), BAD_REQUEST)

    // true idempotency
    @DeleteMapping(CPF_URL)
    fun delete(@PathVariable cpf: String) =
        if (cpf.isValidCpf()) {
            val remainingBalance = personService.deleteByCpf(cpf)
            if (!remainingBalance.isPresent) ResponseEntity(OK)
            else ResponseEntity(mapOf(ERROR to BALANCE_NOT_SETTLED), BAD_REQUEST)
        } else
            ResponseEntity(mapOf(ERROR to INVALID_CPF), BAD_REQUEST)
}
