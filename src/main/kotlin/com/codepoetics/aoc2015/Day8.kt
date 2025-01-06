package com.codepoetics.aoc2015

import com.codepoetics.aoc.inputLines

val String.unescaped: String get() {
    val iter = iterator()
    var prev = iter.next()
    var ignoreCount = 0
    var isEscaping = false
    val sb = StringBuilder()
    while (iter.hasNext()) {
        val next = iter.next()
        if (ignoreCount > 0) {
            ignoreCount--
            if (ignoreCount == 0) {
                sb.append("$prev$next".toInt(16).toChar())
            }
            prev = next
            continue
        }
        if (isEscaping) {
            isEscaping = false
            when(next) {
                '\\', '"' -> sb.append(next)
                'x' -> ignoreCount = 2
                else -> error("Invalid escape sequence $prev$next in $this")
            }
        } else {
            when(next) {
                '\\' -> isEscaping = true
                '"' -> {}
                else -> sb.append(next)
            }
        }
        prev = next
    }
    return sb.toString()
}

val String.escaped: String get() {
    val sb = StringBuilder()
    sb.append('"')
    forEach { c ->
        when(c) {
            '"' -> sb.append("\\\"")
            '\\' -> sb.append("\\\\")
            else -> sb.append(c)
        }
    }
    sb.append('"')
    return sb.toString()
}

fun main() {
    val lines = inputLines("/day8.txt").toList()
    val part1 = lines.sumOf { it.length - it.unescaped.length }
    val part2 = lines.sumOf { it.escaped.length - it.length }

    println("part1: $part1")
    println("part2: $part2")
}
