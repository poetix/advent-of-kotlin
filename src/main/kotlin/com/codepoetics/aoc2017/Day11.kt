package com.codepoetics.aoc2017

import com.codepoetics.aoc.HexDirection
import com.codepoetics.aoc.HexPoint
import com.codepoetics.aoc.inputLines
import com.codepoetics.mariko.kotlin.interpret
import kotlin.math.max

fun main() {
    val directions = inputLines("/2017/day11.txt").first()
        .splitToSequence(",")
        .interpret<HexDirection>()
        .toList()

    val (endpoint, part2) = directions.fold(HexPoint.ORIGIN to 0L) { (p, maxSize), d ->
        d.transform(p).let { it to max(maxSize, it.size) }
    }

    val part1 = endpoint.size
    println("part1: $part1")
    println("part2: $part2")
}