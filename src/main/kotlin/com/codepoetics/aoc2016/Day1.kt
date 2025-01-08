package com.codepoetics.aoc2016

import com.codepoetics.aoc.NORTH
import com.codepoetics.aoc.ORIGIN
import com.codepoetics.aoc.Point
import com.codepoetics.aoc.inputLines

fun main() {
    val instructions = inputLines("/2016/day1.txt").first().split(", ")
    val traversed = positions(instructions).toList()

    val part1 = traversed.last().manhattanDistanceFrom(ORIGIN)
    val part2 = firstVisitedTwice(traversed).manhattanDistanceFrom(ORIGIN)

    println("part1: $part1")
    println("part2: $part2")
}

private fun firstVisitedTwice(positions: List<Point>): Point {
    val visited = mutableSetOf<Point>();
    positions.forEach { p ->
        if (p in visited) return p
        visited.add(p)
    }
    error("No position visited twice")
}

private fun positions(instructions: List<String>): Sequence<Point> = sequence {
    var position = Point(0, 0)
    var direction = NORTH
    instructions.forEach { move ->
        direction = if (move.first() == 'L') direction.rotate90Left() else direction.rotate90Right()
        (1..move.substring(1).toInt()).forEach { _ ->
            position += direction
            yield(position)
        }
    }
}