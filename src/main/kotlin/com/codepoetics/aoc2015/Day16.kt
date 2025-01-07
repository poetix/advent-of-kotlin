package com.codepoetics.aoc2015

import com.codepoetics.aoc.inputLines

val sueFacts = mapOf(
    "children" to 3,
    "cats" to 7,
    "samoyeds" to 2,
    "pomeranians" to 3,
    "akitas" to 0,
    "vizslas" to 0,
    "goldfish" to 5,
    "trees" to 3,
    "cars" to 2,
    "perfumes" to 1
)

fun main() {
    val pattern = Regex("([a-z]+): (\\d+)")
    val candidates = inputLines("/day16.txt").mapIndexed { i, line ->
        pattern.findAll(line).map { characteristic ->
            val (name, number) = characteristic.destructured
            name to number.toInt()
        }.toMap() + ("sue" to i + 1)
    }.toList()

    fun part1Matches(characteristic: String, value: Int) = sueFacts[characteristic]?.equals(value) ?: true
    fun part2Matches(characteristic: String, value: Int) = when(characteristic) {
        "cats", "trees" -> value > sueFacts[characteristic]!!
        "pomeranians", "goldfish" -> value < sueFacts[characteristic]!!
        else -> sueFacts[characteristic]?.equals(value) ?: true
    }

    val part1 = candidates.indexOfFirst { characteristics ->
        characteristics.all { (k, v) -> part1Matches(k, v) }
    } + 1

    val part2 = candidates.indexOfFirst { characteristics ->
        characteristics.all { (k, v) -> part2Matches(k, v) }
    } + 1

    println("part1: $part1")
    println("part2: $part2")
}