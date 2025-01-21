package com.codepoetics.aoc2016

import com.codepoetics.aoc.chineseRemainderTheorem

private fun findAlignment(vararg discs: Pair<Int, Int>) =
    chineseRemainderTheorem(discs.mapIndexed { i, (position, modulus) ->
        (position + i + 1) to modulus
    })

fun main() {
    val part1 = findAlignment(
        2 to 5,
        7 to 13,
        10 to 17,
        2 to 3,
        9 to 19,
        0 to 7)

    println("part1: $part1")

    val part2 = findAlignment(
        2 to 5,
        7 to 13,
        10 to 17,
        2 to 3,
        9 to 19,
        0 to 7,
        0 to 11)

    println("part2: $part2")
}