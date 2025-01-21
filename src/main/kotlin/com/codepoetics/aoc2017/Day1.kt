package com.codepoetics.aoc2017

import com.codepoetics.aoc.inputLines

fun main() {
    val input = inputLines("/2017/day1.txt").first().map { it.digitToInt() }
    val part1 = input.asSequence()
        .windowed(2)
        .sumOf { (a, b) -> if (a == b) a else 0 } +
            if (input.first() == input.last()) input.first() else 0

    println("part1: $part1")

    val part2 = input.asSequence()
        .mapIndexed { i, a ->
            val b = input[(i + (input.size / 2)) % input.size]
            if (a == b) a else 0
        }.sum()

    println("part2: $part2")
}