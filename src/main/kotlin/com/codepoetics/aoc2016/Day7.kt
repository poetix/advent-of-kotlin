package com.codepoetics.aoc2016

import com.codepoetics.aoc.inputLines

private fun String.containsAbba() = asSequence().windowed(4).any { (a, b, c, d) ->
    a == d && b == c && a != b
}

private fun String.candidateBabs(): Sequence<String> =
    asSequence().windowed(3).mapNotNull { (a, b, c) -> if (a == c) "$b$a$b" else null }

private fun String.foundBabs(): Sequence<String> =
    asSequence().windowed(3).mapNotNull { (a, b, c) -> if (a == c) "$a$b$a" else null }

fun main() {
    val input = inputLines("/2016/day7.txt").map { line ->
        line.split(Regex("[\\[\\]]")).toList()
    }.toList()

    val part1 = input.count { segments ->
        var hasAbbaOutsideBrackets = false
        var hasAbbaInBrackets = false
        var isInBrackets = false

        for (segment in segments) {
            if (segment.containsAbba()) {
                if (isInBrackets) hasAbbaInBrackets = true else hasAbbaOutsideBrackets = true
            }
            isInBrackets = !isInBrackets
        }
        !hasAbbaInBrackets && hasAbbaOutsideBrackets
    }

    val part2 = input.count { segments ->
        val candidateBabs = mutableSetOf<String>()
        val foundBabs = mutableSetOf<String>()
        var isInBrackets = false

        for (segment in segments) {
            if (isInBrackets) segment.foundBabs().forEach(foundBabs::add)
            else segment.candidateBabs().forEach(candidateBabs::add)
            isInBrackets = !isInBrackets
        }
        candidateBabs.any { it in foundBabs }
    }

    println("part1: $part1")
    println("part2: $part2")
}