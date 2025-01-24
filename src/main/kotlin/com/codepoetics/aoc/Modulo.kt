package com.codepoetics.aoc

import kotlin.math.abs

fun gcd(a: Long, b: Long): Long = if (b == 0L) abs(a) else gcd(b, a % b)

fun lcm(a: Long, b: Long): Long = abs(a * b) / gcd(a, b)

fun modularInverse(a: Long, m: Long): Long {
    fun gcd(a: Long, b: Long): Pair<Long, Long> =
        if (b == 0L) 1L to 0L else {
            val (x1, y1) = gcd(b, a % b)
            y1 to x1 - (a / b) * y1
        }

    val (x, _) = gcd(a, m)
    if (a % m == 0L) error("a=$a and m=$m are not coprime")
    return (x % m + m) % m
}

fun extendedCRT(congruences: List<Pair<Long, Long>>): Pair<Long, Long> {
    require(congruences.isNotEmpty()) { "Congruences list must not be empty" }

    var currentRemainder = congruences[0].first
    var currentModulus = congruences[0].second

    for (i in 1 until congruences.size) {
        val (nextRemainder, nextModulus) = congruences[i]
        val g = gcd(currentModulus, nextModulus)

        if ((nextRemainder - currentRemainder) % g != 0L) error("No solution exists")

        val lcm = lcm(currentModulus, nextModulus)

        val inv = modularInverse(currentModulus / g, nextModulus / g)
        val step = (nextRemainder - currentRemainder) / g * inv % (nextModulus / g)
        currentRemainder = (currentRemainder + step * currentModulus) % lcm
        if (currentRemainder < 0) currentRemainder += lcm

        currentModulus = lcm
    }

    return Pair(currentRemainder, currentModulus)
}

fun chineseRemainderTheorem(congruences: List<Pair<Long, Long>>): Long {
    val bigM = congruences.asSequence().map { it.second }.fold(1, Long::times)

    var solution = 0L
    for ((s, m) in congruences) {
        val ai = (-s).mod(m)
        val bigMForI = bigM / m
        val bigMInverse = modularInverse(bigMForI, m)
        solution += ai * bigMForI * bigMInverse
    }

    return solution % bigM
}