package com.repository

import com.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepo : JpaRepository<Person, Int> {
    fun findByCpf(cpf: String): Person?
}