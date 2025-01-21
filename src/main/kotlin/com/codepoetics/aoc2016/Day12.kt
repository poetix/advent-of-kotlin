package com.codepoetics.aoc2016

// Likely not valid for all inputs - is a redux of the machine code in mine
fun run(initialC: Int): Int {
    var a = 1
    var b = 1
    var d = 26
    if (initialC != 0) d += 7

    (0..<d).forEach { _ ->
        val c = a
        a += b
        b = c
    }

    a += 11 * 18
    return a
}

fun main() {
    val part1 = run(0)
    println("part1: $part1")

    val part2 = run(1)
    println("part2: $part2")
}