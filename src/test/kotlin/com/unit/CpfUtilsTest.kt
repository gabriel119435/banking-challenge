package com.unit

import com.util.generateCpf
import com.util.isValidCpf
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CpfUtilsTest {

    val validCpfs = setOf(
        "74138265007",
        "16026467408",
        "20866873465",
        "12740531702",
        "67418878141",
        "36573037154",
        "86708102390",
        "58002027108",
        "31042754322",
        "58484163709"
    )

    @Test
    fun `validate 10 fixed cpfs`() = validCpfs.forEach { cpf -> assertTrue(cpf.isValidCpf(), "$cpf is not valid!") }

    @Test
    fun `generate 10 valid cpfs with same beginning`() =
        validCpfs.forEach { cpf -> assertEquals(cpf, generateCpf(cpf.dropLast(2))) }

    @Test
    fun `check for invalid cpfs`() =
        validCpfs.forEach {
            val lastDigit = it.last().toString().toInt()
            val lastDigitIncremented = if (lastDigit == 9) "0" else (lastDigit + 1).toString()
            val invalidCpf = it.dropLast(1) + lastDigitIncremented
            assert(invalidCpf.length == 11) { "wrong cpf length ${invalidCpf.length}" }
            assertFalse(
                invalidCpf.isValidCpf(),
                "old cpf $it new $invalidCpf is valid!"
            )
        }

    @Test
    fun `check 100 random valid cpfs`() {
        repeat(100) {
            val newRandomCpf = generateCpf()
            assertTrue(newRandomCpf.isValidCpf(), "$newRandomCpf was invalid!")
        }
    }
}