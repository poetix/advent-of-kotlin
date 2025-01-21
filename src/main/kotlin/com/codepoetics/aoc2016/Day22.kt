package com.codepoetics.aoc2016

import com.codepoetics.aoc.*
import com.codepoetics.mariko.api.FromPattern
import com.codepoetics.mariko.kotlin.interpret

@FromPattern("/dev/grid/node-(x\\d+-y\\d+)\\s+\\d+T\\s+(\\d+)T\\s+(\\d+)T.*\\s+(\\d+)%")
data class Node(
    @FromPattern("x(\\d+)-y(\\d+)") val position: Point,
    val used: Int,
    val avail: Int)

fun main() {
    val nodes = inputLines("/2016/day22.txt").drop(2)
        .interpret<Node>().toList()

    val nodesByUsed = nodes.filter { it.used > 0 }.sortedBy { it.used }
    val nodesByAvail = nodes.sortedByDescending { it.avail }
    val part1 = nodesByUsed.sumOf { nodeA ->
        nodesByAvail.asSequence().filter { it != nodeA }
            .takeWhile { it.avail > nodeA.used }
            .count()
    }

    println("part1: $part1")

    // General-purpose solving is impractical here: we print out the grid and work it out by hand
    val grid = nodes.associate { (position, used, avail) ->
        position to when {
            used == 0 -> '_'
            (avail * 100 / (used + avail)) < 5 -> '#'
            else -> '.'
        }
    }

    for (y in 0..grid.maxOf { (p, ) -> p.y }) {
        val line = (0..grid.maxOf { (p, _) -> p.x }).joinToString("") { x ->
            grid[Point(x, y)]!!.toString()
        }
        println(line)
    }

    val moveEmptyToTopRight = 26
    val leftLeg = (33 * 5) + 1
    println("part2: ${moveEmptyToTopRight + leftLeg}")

}