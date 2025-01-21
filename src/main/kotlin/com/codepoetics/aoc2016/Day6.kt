package com.codepoetics.aoc2016

import com.codepoetics.aoc.inputLines
import com.codepoetics.aoc.transpose

private fun Collection<Collection<Char>>.byFrequency(select: Map<Char, Int>.() -> Map.Entry<Char, Int>) =
    map { chars ->
        chars.groupingBy { it }.eachCount().select().let { (k, _) -> k }
    }.joinToString("")

fun main() {
    val input = inputLines("/2016/day6.txt").map { line -> line.asSequence().toList() }
        .toList().transpose()
    val part1 = input.byFrequency { maxBy { (_, v) -> v } }
    val part2 = input.byFrequency { minBy { (_, v) -> v } }

    println("part1: $part1")
    println("part2: $part2")
}