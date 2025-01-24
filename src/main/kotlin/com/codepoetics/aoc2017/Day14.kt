package com.codepoetics.aoc2017

import com.codepoetics.aoc.DisjointSet
import com.codepoetics.aoc.Point

fun usedSquares(key: String): Sequence<Point> =
    (0..127).asSequence().flatMap { y ->
        val key = "$key-$y"
        val hash = key.knotHash()
        (0..127).asSequence().mapNotNull { x ->
            val hashChar = hash[x shr 2]
            val hashByte = hashChar.digitToInt(16)
            val testBit = 8 shr (x and 3)
            if (hashByte and testBit > 0) Point(x, y) else null
        }
    }

fun main() {
    val used = usedSquares("vbqugkhl").toSet()

    val part1 = used.size
    println("part1: $part1")

    val disjointSet = DisjointSet<Point>().apply {
        used.forEach { p ->
            add(p)
            p.nsew.filter(used::contains).forEach { a ->
                add(a)
                connect(a, p)
            }
        }
    }

    val part2 = disjointSet.groupCount();
    println("part2: $part2")
}