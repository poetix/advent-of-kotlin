package com.codepoetics.aoc2017

import com.codepoetics.aoc.inputLines
import com.codepoetics.mariko.api.FromPattern
import com.codepoetics.mariko.kotlin.interpret

@FromPattern("(\\d+): (\\d+)")
data class Scanner(val depth: Long, val range: Long) {
    val cycleLength: Long = (range - 1) * 2

    fun isAtZero(picosecond: Long): Boolean {
        return picosecond % cycleLength == 0L
    }

    val positionAtDepth: Long get() {
        val positionInCycle = depth % cycleLength
        return if (positionInCycle < range) positionInCycle else (range - 2) + (positionInCycle - range)
    }
}

fun main() {
    val scanners = inputLines("/2017/day13.txt").interpret<Scanner>().toList()
    val part1 = scanners.sumOf { if (it.isAtZero(it.depth)) it.depth * it.range else 0 }
    println("part1: $part1")

    // ugh
    val part2 = (0..10000000).first { delay ->
        scanners.count { it.isAtZero(it.depth + delay) } == 0
    }
    println("part2: $part2")
}