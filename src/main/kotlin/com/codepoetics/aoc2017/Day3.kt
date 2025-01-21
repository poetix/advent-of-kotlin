package com.codepoetics.aoc2017

import com.codepoetics.aoc.ORIGIN
import com.codepoetics.aoc.Point
import kotlin.math.floor
import kotlin.math.sqrt

fun positionOf(index: Int): Point {
    val rank = floor((sqrt(index.toDouble()) - 1) / 2).toInt()

    val endOfPreviousRank = (1 + ((rank) * 2)).let { it * it }
    if (index == endOfPreviousRank) return Point(rank, rank)

    val edge = rank + 1
    val endOfRank = (1 + (edge * 2)).let { it * it }
    val lengthOfHorizontal = 1 + (edge * 2)
    val lengthOfVertical = lengthOfHorizontal - 2

    var diff = endOfRank - index
    if (diff < lengthOfHorizontal) return Point(edge - diff, edge)

    diff -= lengthOfHorizontal
    if (diff < lengthOfVertical) return Point(-edge, edge - (diff + 1))

    diff -= lengthOfVertical
    if (diff < lengthOfHorizontal) return Point(-edge + diff, -edge)

    diff -= lengthOfVertical
    return Point(edge, -edge + diff - 1)
}

fun main() {
    val part1 = positionOf(368078).manhattanDistanceFrom(ORIGIN)
    println("part1: $part1")

    val filled = mutableMapOf<Point, Int>().also { it[ORIGIN] = 1 }
    var index = 2
    while (true) {
        val pos = positionOf(index++)
        val value = pos.adjacent.sumOf { filled[it] ?: 0 }
        if (value > 368078) {
            println("part2: $value")
            break
        }
        filled[pos] = value
    }
}