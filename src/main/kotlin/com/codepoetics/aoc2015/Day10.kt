package com.codepoetics.aoc2015

fun String.lookAndSay(): String {
    val iter = iterator()
    val sb = StringBuilder()
    var current = iter.next()
    var count = 1
    while (iter.hasNext()) {
        val next = iter.next()
        if (next == current) {
            count++
        } else {
            sb.append(count).append(current)
            current = next
            count = 1
        }
    }
    sb.append(count).append(current)
    return sb.toString()
}

fun main() {
    val after40 = (0 until 40).fold("1113222113") { s, _ -> s.lookAndSay() }

    println("part1: ${after40.length}")

    val after50 = (0 until 10).fold(after40) { s, _ -> s.lookAndSay() }

    println("part2: ${after50.length}")
}