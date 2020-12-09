package com.service

import com.model.Account
import com.model.Person
import com.repository.AccountRepo
import com.repository.PersonRepo
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class PersonService(
    val personRepo: PersonRepo,
    val accountRepo: AccountRepo
) {
    fun getAll(): List<Person> = personRepo.findAll()
    fun findByCpf(cpf: String): Person? = personRepo.findByCpf(cpf)

    fun save(person: Person): Person =
        personRepo.save(person).apply {
            accountRepo.save(
                Account(
                    cpf = this.cpf,
                    balance = BigDecimal.ZERO
                )
            )
        }

    // returns how much must be settled before closing the account
    fun deleteByCpf(cpf: String): Optional<BigDecimal> =
        personRepo.findByCpf(cpf)?.let { person ->
            // if a person exists without an account, NPE will be thrown
            return accountRepo.findByCpf(cpf)!!.let { acc ->
                if (BigDecimal.ZERO == acc.balance) {
                    accountRepo.delete(acc)
                    personRepo.delete(person)
                    Optional.empty()
                } else {
                    Optional.of(acc.balance)
                }
            }
        } ?: Optional.empty() // no person was found, delete successful
}