package com.codepoetics.aoc2016

import com.codepoetics.aoc.*

fun makeKeypad(vararg rows: String): Map<Point, Char> =
    rows.asSequence().flatMapIndexed { y, untrimmed ->
        untrimmed.trim().let { row ->
            row.mapIndexed { x, c ->
                Point((x - (row.length / 2)).toLong(), (y - (rows.size / 2)).toLong()) to c
            }
        }
    }.toMap()

private val keypad1 = makeKeypad(
    "123",
    "456",
    "789")

private val keypad2 = makeKeypad(
    "  1  ",
    " 234 ",
    "56789",
    " ABC ",
    "  D  "
)

private fun List<Point>.toKey(keypad: Map<Point, Char>): Char = fold(ORIGIN) { p, d ->
    (p + d).let { if (it in keypad) it else p }
}.let { keypad[it]!! }

fun main() {
    val instructions = inputLines("/2016/day2.txt").map { line ->
        line.map(::udlrFrom)
    }.toList()

    val part1 = instructions.joinToString("") { it.toKey(keypad1).toString() }
    val part2 = instructions.joinToString("") { it.toKey(keypad2).toString() }

    println("part1: $part1")
    println("part2: $part2")
}