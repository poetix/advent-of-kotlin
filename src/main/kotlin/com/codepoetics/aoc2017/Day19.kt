package com.codepoetics.aoc2017

import com.codepoetics.aoc.*

private val Point.isHorizontal: Boolean get() = y == 0L
private val Point.isVertical: Boolean get() = x == 0L

fun main() {
    val grid = inputLines("/2017/day19.txt")
        .map { line -> line.toCharArray() }
        .toList()
        .toTypedArray()

    val width = grid.first().size
    val height = grid.size
    val gridBounds = Region(ORIGIN, Point(width, height))

    fun charAt(p: Point): Char =
        if (p.x in 0..<width && p.y in 0..<height) grid[p.y.toInt()][p.x.toInt()]
        else ' '

    fun isTerminus(p: Point, direction: Point): Boolean {
        if (charAt(p) == '+') return true
        return charAt(p) in 'A'..'Z' && charAt(p + direction) == ' '
    }

    var position = Point(grid.first().indexOf('|'), 0)
    var direction = SOUTH
    val visited = mutableListOf<Char>()
    var stepCount = 0
    while (true) {
        stepCount++
        val charAtPosition = charAt(position)
        if (charAtPosition.isLetter()) visited.add(charAtPosition)

        val nextPosition = position + direction
        if (nextPosition !in gridBounds || charAt(nextPosition) == ' ') break

        position = nextPosition
        if (isTerminus(nextPosition, direction)) {
            val turnLeft = direction.rotate90Left()
            direction = if ((turnLeft.isVertical && charAt(position + turnLeft) !in "- ") ||
                (turnLeft.isHorizontal && charAt(position + turnLeft) !in "| ")) turnLeft
            else direction.rotate90Right()
        }
    }

    val part1 = visited.joinToString("")
    println("part1: $part1")

    val part2 = stepCount
    println("part2: $part2")
}