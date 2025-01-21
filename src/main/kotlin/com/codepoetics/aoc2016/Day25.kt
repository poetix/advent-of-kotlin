package com.codepoetics.aoc2016

fun main() {

    val part1 =  (0..1000).first { i ->
        var a = 2555 + i
        val sequenceToZero = sequence {
            while (a > 0) {
                yield(a % 2)
                a /= 2
            }
        }
        sequenceToZero.chunked(2).all { (a, b) -> a == 0 && b == 1 }
    }

    println("part1: $part1")
}

