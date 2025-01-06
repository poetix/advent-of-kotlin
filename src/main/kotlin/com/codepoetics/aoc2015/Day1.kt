package com.codepoetics.aoc2015

import com.codepoetics.aoc.inputLines

fun main() {
    val input = inputLines("/day1.txt").first()

    val (part2, part1) = input.foldIndexed(0 to 0) { index, (basementIndex, floor), c -> when(c) {
        '(' -> basementIndex to floor + 1
        ')' -> (if (floor == 0 && basementIndex == 0) index + 1 else basementIndex) to floor - 1
        else -> error("Unrecognised character $c")
    } }

    println("Part 1: $part1")
    println("Part 2: $part2")
}