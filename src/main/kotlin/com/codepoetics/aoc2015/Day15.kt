package com.codepoetics.aoc2015

import com.codepoetics.aoc.inputLines
import kotlin.math.max

fun recipes(): Sequence<List<Int>> =
    (0..100).asSequence().flatMap { f ->
        (0..(100 - f)).asSequence().flatMap { c ->
            (0..(100 - f - c)).asSequence().map { b -> listOf(f, c, b, 100 - f - c - b) }
        }
    }

fun main() {
    val pattern = Regex("(-?\\d+)")
    val (frosting, candy, butterscotch, sugar) = inputLines("/day15.txt").map { line ->
        pattern.findAll(line).map { it.value.toInt() }.toList()
    }.toList()

    fun score(amounts: List<Int>): Int = (0..3).map { characteristic ->
        max(0, frosting[characteristic] * amounts[0]
                + candy[characteristic] * amounts[1]
                + butterscotch[characteristic] * amounts[2]
                + sugar[characteristic] * amounts[3])
    }.reduce(Int::times)

    fun calories(amounts: List<Int>): Int =
        frosting[4] * amounts[0] +
                candy[4] * amounts[1] +
                butterscotch[4] * amounts[2]+
                sugar[4] * amounts[3]

    val part1 = recipes().map(::score).max()

    val part2 = recipes()
        .filter { amounts -> calories(amounts) == 500 }
        .map(::score)
        .max()

    println("part1: $part1")
    println("part2: $part2")
}