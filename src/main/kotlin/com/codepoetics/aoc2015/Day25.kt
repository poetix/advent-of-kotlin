package com.codepoetics.aoc2015

fun count(row: Int, col: Int): Long =
    ((row + col - 2) * (row + col - 1) / 2 + col - 1).toLong()

fun Long.modExp(exponent: Long, modulo: Long): Long {
    var x = 1L
    var y = exponent
    var mutB = exponent
    while (mutB > 0L) {
        if (mutB % 2 == 1L) {
            x = (x * y) % modulo
        }
        y = (y * y) % modulo
        mutB = mutB shr 1
    }
    return x % modulo
}

fun main() {
    val part1 = (252533L.modExp(count(3010, 3019), 33554393L) * 20151125) % 33554393L
    println("part1: $part1")
}