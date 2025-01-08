package com.codepoetics.aoc2015

import com.codepoetics.aoc.Point
import com.codepoetics.aoc.Region
import com.codepoetics.aoc.inputLines

fun Sequence<String>.pointsIn(): Set<Point> = flatMapIndexed { y, line ->
    line.mapIndexedNotNull { x, c -> if (c=='#') Point(x.toLong(), y.toLong()) else null }
}.toSet()

fun main() {
    val on = inputLines("/day18.txt").pointsIn()

    val corners = setOf(Point(0, 0), Point(0, 99), Point(99, 0), Point(99, 99))

    val part1 = (0 until 100).fold(on) { acc, _ -> generation(acc) }.size
    val part2 = (0 until 100).fold(on + corners) { acc, _ -> generation(acc) + corners }.size

    println("part1: $part1")
    println("part2: $part2")
}

private val gridRegion = Region(Point(0, 0), Point(99, 99))

fun generation(on: Set<Point>): Set<Point> =
    on.asSequence()
        .flatMap { p -> p.adjacent.filter { it in gridRegion} }
        .groupingBy { it }
        .eachCount()
        .asSequence()
        .filter { (point, count) ->
            if (point in on) count in 2..3 else count == 3
        }.map { it.key }
        .toSet()