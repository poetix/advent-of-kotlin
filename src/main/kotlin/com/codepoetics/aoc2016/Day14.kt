package com.codepoetics.aoc2016

import com.codepoetics.aoc.md5

fun main() {
    val part1 = findKeys("ngcjuoqr", false)
    val part2 = findKeys("ngcjuoqr", true)

    println("part1: $part1")
    println("part2: $part2")
}

private fun findKeys(salt: String, stretch: Boolean): Int {
    val trips = mutableListOf<Pair<Int, Char>>()
    val quints = mutableMapOf<Char, MutableSet<Int>>()

    (0..30000).forEach { i ->
        val firstHash = "$salt$i".md5()
        val hash = if (stretch) (1..2016).fold(firstHash) { h, _ -> h.md5() } else firstHash

        val tripChar = hash.asSequence().windowed(3).filter { (a, b, c) ->
            a == b && b == c
        }.map { it.first() }.firstOrNull()

        if (tripChar != null) {
            trips.add(i to tripChar)

            hash.asSequence().windowed(5).forEach { (a, b, c, d, e) ->
                if (a == b && b == c && c == d && d == e) {
                    quints.computeIfAbsent(a) { mutableSetOf() }.add(i)
                }
            }
        }
    }

    val keys = trips.asSequence().filter { (index, tripChar) ->
        quints[tripChar]?.any { it - index in 1..1000 } ?: false
    }.take(64).toList()

    if (keys.size < 64) error("Need more keys!")

    return keys.last().first
}