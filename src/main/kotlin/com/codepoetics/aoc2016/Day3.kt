package com.codepoetics.aoc2016

import com.codepoetics.aoc.inputLines
import com.codepoetics.aoc.transpose

private fun List<Int>.isPossibleTriangle(): Boolean = let { (a, b, c) ->
    a + b > c
}

fun main() {
    val rows = inputLines("/2016/day3.txt")
        .map { it.trim().split(Regex("\\s+")).asSequence().map(String::toInt).toList() }
        .toList()

    val part1 = rows.count { it.sorted().isPossibleTriangle() }

    println("part1: $part1")

    val part2 = rows.asSequence()
        .chunked(3)
        .flatMap { it.transpose() }
        .count { it.sorted().isPossibleTriangle() }

    println("part2: $part2")
}