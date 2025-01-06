package com.codepoetics.aoc2015

import com.codepoetics.aoc.inputLines

data class Box(val l: Int, val w: Int, val h: Int) {

    fun paperRequired(): Int {
        val sideAreas = listOf(l * w, l * h, w * h)
        val smallestSide = sideAreas.min()
        val surfaceArea = sideAreas.sum() * 2

        return surfaceArea + smallestSide
    }

    fun ribbonRequired(): Int {
        val smallestPerimeter = minOf(l + w, l + h, w + h) * 2
        val volume = l * w * h

        return smallestPerimeter + volume
    }
}

fun main() {
    val boxes = inputLines("/day2.txt").map { line ->
        val (l, w, h) = line.split("x").map { it.toInt() }
        Box(l, w, h)
    }.toList()

    val part1 = boxes.sumOf(Box::paperRequired)
    val part2 = boxes.sumOf(Box::ribbonRequired)

    println("part1: $part1")
    println("part2: $part2")
}