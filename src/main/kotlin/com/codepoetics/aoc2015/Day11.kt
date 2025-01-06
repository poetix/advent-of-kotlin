package com.codepoetics.aoc2015

import kotlin.streams.asSequence

private val forbidden = setOf('i', 'l', 'o')
private val permitted = ('a'..'z').filterNot { it in forbidden }.toList()

fun String.increment(): String {
    val asArray = toCharArray()
    var currentIndex = asArray.lastIndex
    while (currentIndex >= 0) {
        val indexOfCurrent = permitted.indexOf(asArray[currentIndex])
        if (indexOfCurrent < permitted.lastIndex) {
            asArray[currentIndex] = permitted[indexOfCurrent + 1]
            break
        }
        asArray[currentIndex--] = permitted.first()
    }
    return String(asArray)
}

fun String.isValidPassword(): Boolean {
    val pairPositions = asSequence().windowed(2).mapIndexedNotNull { i, (a, b) ->
        if (a==b) i else null
    }.toList()
    if (pairPositions.size < 2) return false
    if (pairPositions.size == 2 && pairPositions[1] == pairPositions[0] + 1) return false

    return chars().asSequence().windowed(3).any { (a, b, c) ->
        b - a == 1 && c - b == 1
    }
}

fun main() {
    var initial = "hxbxwxba"
    while (!initial.isValidPassword()) {
        initial = initial.increment()
    }

    println("part1: $initial")

    initial = initial.increment()
    while (!initial.isValidPassword()) {
        initial = initial.increment()
    }
    println("part2: $initial")
}