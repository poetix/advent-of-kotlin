package com.codepoetics.aoc2015

import com.codepoetics.aoc.discretePairs
import com.codepoetics.aoc.inputLines
import com.codepoetics.aoc.toListMultimap

fun Char.isVowel(): Boolean = when(this) {
    'a', 'e', 'i', 'o', 'u' -> true
    else -> false
}

private val illegalPairs = setOf("ab", "cd", "pq", "xy")

fun String.isNice(): Boolean {
    val vowelCount = asSequence().count { it.isVowel() }

    var hasDup = false
    asSequence().windowed(2).forEach { (first, second) ->
        if ("$first$second" in illegalPairs) return false
        if (first == second) hasDup = true
    }

    return hasDup && vowelCount >= 3
}

fun String.isNice2(): Boolean {
    if (asSequence().windowed(3).none { (a, _, c) -> a == c }) return false

    val pairPositions = asSequence().windowed(2)
        .mapIndexed { i, (a, b) -> "$a$b" to i }
        .toListMultimap()

    return pairPositions.values.any { positions ->
        positions.discretePairs().any { (first, second) -> second - first > 1 }
    }
}


fun main() {
    val strings = inputLines("/day5.txt").toList()
    val part1 = strings.count { it.isNice() }
    val part2 = strings.count { it.isNice2() }

    println("part1: $part1")
    println("part2: $part2")
}