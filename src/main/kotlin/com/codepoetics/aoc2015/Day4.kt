package com.codepoetics.aoc2015

import com.codepoetics.aoc.md5

fun main() {
    val prefix = "bgvyzdsv"
    val part1 = (0L until Long.MAX_VALUE).find { "$prefix$it".md5().startsWith("00000") }!!
    val part2 = (0L until Long.MAX_VALUE).find { "$prefix$it".md5().startsWith("000000") }!!

    println("part1: $part1")
    println("part2: $part2")
}