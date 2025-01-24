package com.codepoetics.aoc2017

import com.codepoetics.aoc.inputLines

fun IntArray.reverseCircular(start: Int, length: Int) {
    var left = start
    var right = start + length - 1
    while (left < right) {
        val swap = this[left % size]
        this[left % size] = this[right % size]
        this[right % size] = swap
        left++
        right--
    }
}

fun hash(lengths: List<Int>, times: Int): IntArray {
    val values = (0..255).toList().toIntArray()
    var pos = 0
    var skip = 0
    repeat(times) {
        lengths.forEach { length ->
            values.reverseCircular(pos, length)
            pos = (pos + length + skip) % values.size
            skip++
        }
    }
    return values
}

fun String.knotHash(): String {
    val lengths = map { it.code } + listOf(17, 31, 73, 47, 23)
    val hashed = hash(lengths, 64)
    return hashed.asSequence().chunked(16)
        .map { chunk -> chunk.reduce { a, b -> a xor b } }
        .joinToString("") { "%02X".format(it) }
}

fun main() {
    val input = inputLines("/2017/day10.txt").first()
    val part1Lengths = input.split(",").map { it.toInt() }
    val hashed = hash(part1Lengths, 1)
    val part1 = hashed[0] * hashed[1]
    println("part1: $part1")

    val part2 = input.knotHash()
    println("part2: $part2")
}
