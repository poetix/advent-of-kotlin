package com.codepoetics.aoc2017

import com.codepoetics.aoc.inputLines

private fun String.score(): Pair<Int, Int> {
    var ignore = false
    var inGarbage = false
    var score = 0
    var depth = 0
    var garbageCharCount = 0
    for (c in this) {
        if (ignore) {
            ignore = false
            continue
        }
        when (c) {
            '<' -> if (!inGarbage) inGarbage = true else garbageCharCount++
            '>' -> inGarbage = false
            '!' -> if (inGarbage) ignore = true
            '{' -> if (!inGarbage) depth++ else garbageCharCount++
            '}' -> if (!inGarbage) {
                score += depth
                depth -= 1
            } else garbageCharCount++
            else -> if (inGarbage) garbageCharCount++
        }
    }
    return score to garbageCharCount
}

fun main() {
    val input = inputLines("/2017/day9.txt").first()
    val (part1, part2) = input.score()
    println("part1: $part1")
    println("part2: $part2")
}