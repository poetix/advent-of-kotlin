package com.codepoetics.aoc2015

import com.codepoetics.aoc.Lst
import com.codepoetics.aoc.inputLines

data class Leg(val from: String, val to: String, val distance: Int)

fun main() {
    val legs = inputLines("/day9.txt").map {
        val (from, _, to, _, distStr) = it.split(" ")
        Leg(from, to, distStr.toInt())
    }

    val costs = mutableMapOf<String, MutableMap<String, Int>>()
    legs.forEach { (from, to, distance) ->
        costs.computeIfAbsent(from) { mutableMapOf() }[to] = distance
        costs.computeIfAbsent(to) { mutableMapOf() }[from] = distance
    }

    val destinations = Lst.of(costs.keys)
    val journeyDistances = destinations.permutations().map { permutation ->
        permutation.asSequence().windowed(2).map { (from, to) -> costs[from]!![to]!! }.sum()
    }.toList()

    val part1 = journeyDistances.min()
    val part2 = journeyDistances.max()

    println("part1: $part1")
    println("part2: $part2")
}