package com.codepoetics.aoc2016

import com.codepoetics.aoc.inputLines

private fun String.nextRow() =
    ".${this}.".asSequence().windowed(3).joinToString("") { (a, b, c) ->
        if ((a == '^' && b == '^' && c == '.') ||
                (a == '.' && b == '^' && c == '^') ||
                (a == '^' && b == '.' && c == '.') ||
                (a == '.' && b == '.' && c == '^')) "^" else "."
    }

fun main() {
    val input = inputLines("/2016/day18.txt").first()

    var row = input
    var safeTotal = row.count { it == '.' }
    for (i in 1..<40) {
        row = row.nextRow()
        safeTotal += row.count { it == '.' }
    }

    val part1 = safeTotal
    println("part1: $part1")

    row = input
    safeTotal = row.count { it == '.' }
    for (i in 1..<400000) {
        row = row.nextRow()
        safeTotal += row.count { it == '.' }
    }

    val part2 = safeTotal
    println("part2: $part2")
}