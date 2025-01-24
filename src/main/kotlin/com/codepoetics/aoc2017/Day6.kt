package com.codepoetics.aoc2017

import com.codepoetics.aoc.inputLines

fun redistribute(blocks: List<Int>): List<Int> {
    val max = blocks.max()
    val idx = blocks.indexOfFirst { it == max }
    val all = max / blocks.size
    val remainder = max % blocks.size

    return blocks.mapIndexed { i, v ->
        val distance = (i + blocks.size - idx) % blocks.size
        (if (i == idx) 0 else v) + all + (if (distance in 1..remainder) 1 else 0)
    }
}

fun main() {
    val input = inputLines("/2017/day6.txt").first()
        .splitToSequence(Regex("\\s+"))
        .map { it.toInt() }
        .toList()

    val visited = mutableSetOf<List<Int>>()
    var part1 = 0
    var current = input

    while (current !in visited) {
        visited.add(current)
        current = redistribute(current)
        part1++
    }

    println("part1: $part1")

    var next = redistribute(current)
    var part2 = 1
    while (next != current) {
        next = redistribute(next)
        part2 += 1
    }

    println("part2: $part2")
}