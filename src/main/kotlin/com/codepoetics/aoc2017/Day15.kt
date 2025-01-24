package com.codepoetics.aoc2017

fun generator(initialValue: Long, factor: Long): Sequence<Long> = sequence {
    var nextValue = (initialValue * factor) % 2147483647L
    while (true) {
        yield(nextValue and 65535L)
        nextValue = (nextValue * factor) % 2147483647L
    }
}

fun main() {
    val genA = generator(722, 16807)
    val genB = generator(354, 48271)

    val part1 = genA.zip(genB).take(40_000_000).count { (a, b) -> a == b }
    println("part1: $part1")

    val part2 = genA.filter { it % 4L == 0L }.zip(genB.filter { it % 8L == 0L})
        .take(5_000_000)
        .count { (a, b) -> a == b }
    println("part2: $part2"  )
}