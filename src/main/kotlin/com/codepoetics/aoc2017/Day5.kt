package com.codepoetics.aoc2017

import com.codepoetics.aoc.inputLines

fun main() {
    val jumps = inputLines("/2017/day5.txt").map { it.toInt() }.toList()

    var ptr = 0
    var steps = 0
    var mutableJumps = jumps.toMutableList()
    while (ptr in mutableJumps.indices) {
        val jmp = mutableJumps[ptr];
        mutableJumps[ptr]++;
        ptr += jmp
        steps++
    }

    println("part1: $steps")

    ptr = 0
    steps = 0
    mutableJumps = jumps.toMutableList()
    while (ptr in mutableJumps.indices) {
        val jmp = mutableJumps[ptr];
        if (jmp >= 3) mutableJumps[ptr]-- else mutableJumps[ptr]++;
        ptr += jmp
        steps++
    }

    println("part2: $steps")
}