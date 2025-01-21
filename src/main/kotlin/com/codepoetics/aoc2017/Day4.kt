package com.codepoetics.aoc2017

import com.codepoetics.aoc.inputLines

fun main() {
    val input = inputLines("/2017/day4.txt").map { it.split(Regex("\\s+")) }.toList()

    val part1 = input.count { it.distinct().size == it.size }
    println("part1: $part1")

    val letterCounts = input.map { words -> words.map { word -> word.groupingBy { it }.eachCount() } }
    val part2 = letterCounts.count { it.distinct().size == it.size }

    println("part2: $part2")
}