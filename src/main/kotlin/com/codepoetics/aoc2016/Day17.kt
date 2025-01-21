package com.codepoetics.aoc2016

import com.codepoetics.aoc.*
import java.util.PriorityQueue
import kotlin.math.max

data class SearchState(val position: Point, val path: String, val passcode: String) {

    fun nextStates(): Sequence<SearchState> = sequence {
        val (a, b, c, d) = "$passcode$path".md5().toCharArray()
        if (a in 'b'..'f' && position.y > 0) yield(SearchState(position + NORTH, path + 'U', passcode))
        if (b in 'b'..'f' && position.y < 3) yield(SearchState(position + SOUTH, path + 'D', passcode))
        if (c in 'b'..'f' && position.x > 0) yield(SearchState(position + WEST, path + 'L', passcode))
        if (d in 'b'..'f' && position.x < 3) yield(SearchState(position + EAST, path + 'R', passcode))
    }

    val score = position.manhattanDistanceFrom(ORIGIN)

    val isFinal: Boolean get() = position == Point(3, 3)
}

fun shortestPath(passcode: String, exitOnFinal: Boolean): String {
    val visited = mutableSetOf<String>()
    val queue = PriorityQueue<Pair<Int, SearchState>>(compareBy<Pair<Int, SearchState>> { it.first }
        .then(compareBy { it.second.score }))
    queue.add(0 to SearchState(ORIGIN, "", passcode))
    visited.add("")

    var maxLength = 0
    while (queue.isNotEmpty()) {
        val (count, next) = queue.remove()
        visited.add(next.path)
        if (next.isFinal) {
            maxLength = max(maxLength, count)
            if (exitOnFinal) return next.path
        } else {
            next.nextStates().filter { it.path !in visited }.forEach { state ->
                queue.add(count + 1 to state)
            }
        }
    }
    return maxLength.toString()
}

fun main() {
    val part1 = shortestPath("vkjiggvb", true)
    val part2 = shortestPath("vkjiggvb", false)
    println("part1: $part1")
    println("part2: $part2")
}