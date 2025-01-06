package com.codepoetics.aoc2015

import com.codepoetics.aoc.Point
import com.codepoetics.aoc.inputLines
import com.codepoetics.aoc.nsewFrom
import kotlin.streams.asSequence

fun main() {
    val directions = inputLines("/day3.txt").first().chars().asSequence().map(::nsewFrom).toList()

    val part1 = part1(directions)
    val part2 = part2(directions)

    println("part1: $part1")
    println("part2: $part2")
}

private fun part1(directions: List<Point>): Int {
    val visited = mutableSetOf<Point>()

    var position = Point(0, 0)
    visited.add(position)

    directions.forEach { direction ->
        position = position.plus(direction)
        visited.add(position)
    }

    return visited.size
}

private fun part2(directions: List<Point>): Int {
    val visited = mutableSetOf<Point>()

    var santaPosition = Point(0, 0)
    var roboPosition = Point(0, 0)
    visited.add(santaPosition)

    val iter = directions.iterator()
    while (iter.hasNext()) {
        santaPosition = santaPosition.plus(iter.next())
        visited.add(santaPosition)

        if (iter.hasNext()) {
            roboPosition = roboPosition.plus(iter.next())
            visited.add(roboPosition)
        }
    }

    return visited.size
}