package com.codepoetics.aoc2016

import com.codepoetics.aoc.md5

fun passwordChars(): Sequence<Char> = (0..Int.MAX_VALUE).asSequence().mapNotNull {
    "ffykfhsq$it".md5().let { hash ->
        if (hash.startsWith("00000")) hash[5] else null
    }
}

fun passwordCharsInPosition(): Sequence<Pair<Int, Char>> = (0..Int.MAX_VALUE).asSequence().mapNotNull {
    "ffykfhsq$it".md5().let { hash ->
        if (hash.startsWith("00000") &&
            hash[5].isDigit() &&
            hash[5].digitToInt() < 8) (hash[5].digitToInt() to hash[6]) else null
    }
}

fun main() {
    val part1 = passwordChars().take(8).joinToString("")
    println("part1: $part1")

    val digits = mutableMapOf<Int, Char>()
    passwordCharsInPosition().takeWhile {
        digits.size < 8
    }.forEach { (pos, char) ->
        digits.putIfAbsent(pos, char)
        val pwd = (0..7).asSequence().map { i -> digits[i] ?: '-' }.joinToString("")
        println("$pos $char $pwd")
    }

    val part2 = (0..7).asSequence().map(digits::get).joinToString("")
    println("part2: $part2")
}