package com.codepoetics.aoc2016

import com.codepoetics.aoc.Point

private fun hasWall(p: Point, puzzleInput: Int): Boolean {
    val n = p.x * p.x + 3 * p.x + 2 * p.x * p.y + p.y + p.y * p.y + puzzleInput
    val countBits = (0..31).count { (n and (1L shl it)) > 0 }
    return (countBits % 2) == 1
}

private fun shortestPathTo(target: Point, puzzleInput: Int, maxSteps: Int): Int {
    val visited = mutableSetOf<Point>()
    val queue = ArrayDeque<Pair<Int, Point>>()
    queue.add(0 to Point(1, 1))

    while (!queue.isEmpty()) {
        val (count, next) = queue.removeFirst()
        if (next == target) return count
        visited.add(next)
        if (count < maxSteps) {
            next.nsew.filter { it.x >= 0 && it.y >= 0 && it !in visited && !hasWall(it, puzzleInput) }
                .forEach { queue.add(count + 1 to it) }
        }
    }

    return visited.size
}

fun main() {
    val part1 = shortestPathTo(Point(31, 39), 1364, 1000)
    println("part1: $part1")

    val part2 = shortestPathTo(Point(51, 51), 1364, 50)
    println("part2: $part2")
}