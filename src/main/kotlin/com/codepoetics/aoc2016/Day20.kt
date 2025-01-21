package com.codepoetics.aoc2016

import com.codepoetics.aoc.InclusiveRange
import com.codepoetics.aoc.differenceAll
import com.codepoetics.aoc.inputLines

fun main() {
    val permitted = inputLines("/2016/day20.txt").filterNot(String::isEmpty).map {
        InclusiveRange(
            it.substringBefore("-").toLong(),
            it.substringAfter("-").toLong())
    }.differenceAll(InclusiveRange(0, 4294967295)).toList()

    val part1 = permitted.minOf(InclusiveRange::startInclusive)
    val part2 = permitted.sumOf { it.endInclusive + 1 - it.startInclusive }

    println("part1: $part1")
    println("part2: $part2")
}