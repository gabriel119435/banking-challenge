package com.util

import java.util.*

fun String.isValidCpf(): Boolean {
    if (this.isBlank() || this.isEmpty()) return false
    val numberList = this.filter { it.isDigit() }.map { c -> c.toString().toInt() }.toList()

    // if it doesn't have 11 digits, not valid
    if (numberList.size != 11) return false

    // if digits are the same, not valid
    if ((0..9).all { numberList[it] == numberList[it + 1] }) return false

    val firstVerifierDigit = numberList.subList(0, 9).toMutableList().getNextVerifierDigit()
    val secondVerifierDigit =
        numberList.subList(0, 9).toMutableList().apply { add(firstVerifierDigit) }.getNextVerifierDigit()

    // comparing verifier digits
    return numberList[numberList.lastIndex - 1] == firstVerifierDigit && numberList[numberList.lastIndex] == secondVerifierDigit
}

private fun MutableList<Int>.getNextVerifierDigit(): Int {
    var multiplier = this.size + 1
    val firstSum = this.sumOf { it * multiplier-- }
    return if (firstSum.rem(11) < 2) 0 else 11 - firstSum.rem(11)
}

/**
 * receives a 9 digit string
 */
fun generateCpf(s: String? = null): String {
    val list = if (s == null) {
        val ran = Random()
        (0..8).map { ran.nextInt(10) }.toMutableList()
    } else {
        s.filter { it.isDigit() }
            .map { it.toString().toInt() }
            .toMutableList()
            .apply { assert(this.size == 9) { "actual size was ${this.size}" } }
    }

    // change some number if they are all the same
    if ((0..9).all { list[it] == list[it + 1] })
        list[0] = if (list[0] == 9) 0 else list[0] + 1

    list.add(list.getNextVerifierDigit())
    list.add(list.getNextVerifierDigit())
    return list.joinToString(separator = "") { it.toString() }
}