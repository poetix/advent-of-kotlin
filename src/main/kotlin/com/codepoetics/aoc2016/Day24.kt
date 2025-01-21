package com.codepoetics.aoc2016

import com.codepoetics.aoc.*

private fun Sequence<String>.toGraph(): Pair<Map<Int, Point>, WeightedGraph<Point>> {
    val positions = mutableMapOf<Int, Point>()

    val openSquares = flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            val p = Point(x.toLong(), y.toLong())
            if (c.isDigit()) positions[c.digitToInt()] = p
            if (c == '.' || c.isDigit()) p else null
        }
    }.toSet()

    val graph = WeightedGraph<Point>()
    openSquares.forEach { p -> p.nsew.filter(openSquares::contains).forEach {
            a -> graph.add(p, a, 1)
        }
    }

    return positions to graph
}

fun shortestPathsFrom(start: Point, targetPositions: Map<Int, Point>, graph: WeightedGraph<Point>): Map<Int, Long> {
    val distanceMap = graph.distanceMap(start)
    return targetPositions.map { (index, position) -> index to distanceMap.distances[position]!! }
        .toMap()
}

fun main() {
    val (positions, graph) = inputLines("/2016/day24.txt").toGraph()

    val distances = positions.map { (index, start) ->
        index to shortestPathsFrom(start, positions, graph)
    }.toMap()

    val possibleOrders = positions.keys.filter { it != 0 }.toLst().permutations().toList()

    val part1 = possibleOrders.minOf { order ->
            (0 cons order).windowed(2).sumOf { (start, finish) ->
                distances[start]!![finish]!!
            }
        }

    println("part1: $part1")

    val part2 = possibleOrders.minOf { order ->
            (0 cons order).windowed(2).sumOf { (start, finish) ->
                distances[start]!![finish]!!
            } + distances[order.last]!![0]!!
        }

    println("part2: $part2")
}