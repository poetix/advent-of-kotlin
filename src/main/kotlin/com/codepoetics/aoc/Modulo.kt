package com.codepoetics.aoc

fun modularInverse(a: Int, m: Int): Int {
    fun gcd(a: Int, b: Int): Pair<Int, Int> =
        if (b == 0) 1 to 0 else {
            val (x1, y1) = gcd(b, a % b)
            y1 to x1 - (a / b) * y1
        }

    val (x, _) = gcd(a, m)
    if (a % m == 0) error("a=$a and m=$m are not coprime")
    return (x % m + m) % m
}

fun chineseRemainderTheorem(congruences: List<Pair<Int, Int>>): Int {
    val bigM = congruences.asSequence().map { it.second }.fold(1, Int::times)

    var solution = 0
    for ((s, m) in congruences) {
        val ai = (-s).mod(m)
        val bigMForI = bigM / m
        val bigMInverse = modularInverse(bigMForI, m)
        solution += ai * bigMForI * bigMInverse
    }

    return solution % bigM
}