package com.codepoetics.aoc2016

import com.codepoetics.aoc.inputLines

private val pattern = Regex("\\((\\d+)x(\\d+)\\)")

fun String.decompressedSize(startIndex: Int, endIndex: Int, recurse: Boolean): Long {
    var i = startIndex
    var totalLength = (endIndex - startIndex).toLong()
    var nextResult = pattern.find(this, i)

    while (nextResult != null && nextResult.range.first < endIndex) {
        val (lengthStr, timesStr) = nextResult.destructured
        val length = lengthStr.toLong()
        val times = timesStr.toLong()

        val decompressedStart = nextResult.range.last + 1
        val tokenLength = decompressedStart - nextResult.range.first

        val decompressedEnd = (decompressedStart + length).toInt()
        val regionLength = if (recurse) decompressedSize(decompressedStart, decompressedEnd, true)
        else length

        totalLength += regionLength * times - tokenLength - length

        i = decompressedEnd
        nextResult = pattern.find(this, i)
    }

    return totalLength
}

fun main() {
    val input = inputLines("/2016/day9.txt").joinToString("")

    val part1 = input.decompressedSize(0, input.length, false)
    val part2 = input.decompressedSize(0, input.length, true)

    println("part1: $part1")
    println("part2: $part2")
}