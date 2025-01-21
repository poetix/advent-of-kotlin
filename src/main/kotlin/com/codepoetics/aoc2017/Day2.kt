package com.codepoetics.aoc2017

import com.codepoetics.aoc.Lst
import com.codepoetics.aoc.inputLines
import com.codepoetics.aoc.toLst

private fun sumOfEvenlyDivisiblePairs(ints: Lst<Int>): Int {
    if (ints.length < 2) return 0
    val a = ints.head
    return ints.tail.sumOf { b ->
        if (a > b && a % b == 0) a / b else
            if (b % a == 0) b / a else 0
    } + sumOfEvenlyDivisiblePairs(ints.tail)
}

fun main() {
    val input = inputLines("/2017/day2.txt").map { line ->
        line.split(Regex("\\s+")).map { it.toInt() }.toLst()
    }.toList()

    val part1 = input.sumOf { values ->
        values.max() - values.min()
    }

    println("part1: $part1")

    val part2 = input.sumOf { values ->
        sumOfEvenlyDivisiblePairs(values)
    }

    println("part2: $part2")
}