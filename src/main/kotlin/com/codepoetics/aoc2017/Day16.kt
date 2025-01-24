package com.codepoetics.aoc2017

import com.codepoetics.aoc.inputLines
import com.codepoetics.mariko.api.FromPattern
import com.codepoetics.mariko.kotlin.interpret

sealed interface DanceInstruction {
    @FromPattern("s(\\d+)")
    data class Spin(val count: Int) : DanceInstruction

    @FromPattern("x(\\d+)/(\\d+)")
    data class Exchange(val a: Int, val b: Int) : DanceInstruction

    @FromPattern("p([a-p])/([a-p])")
    data class Partner(val a: Char, val b: Char) : DanceInstruction

    fun apply(chars: List<Char>) = when (this) {
        is Spin -> chars.subList(chars.size - count, chars.size) + chars.subList(0, chars.size - count)

        is Exchange -> chars.mapIndexed { i, c ->
            when (i) {
                a -> chars[b]
                b -> chars[a]
                else -> c
            }
        }

        is Partner -> {
            val aPos = chars.indexOf(a)
            val bPos = chars.indexOf(b)
            chars.mapIndexed { i, c ->
                when (i) {
                    aPos -> chars[bPos]
                    bPos -> chars[aPos]
                    else -> c
                }
            }
        }
    }
}

@FromPattern(".*")
data class Dance(val instructions: List<DanceInstruction>) {
    fun run(input: List<Char>): List<Char> =
        instructions.fold(input) { chars, instruction -> instruction.apply(chars) }
}

fun main() {
    val dance = inputLines("/2017/day16.txt").first().interpret<Dance>()

    val initialOrder = ('a'..'p').toList()
    val permuted = dance.run(initialOrder)

    val part1 = permuted.joinToString("")
    println("part1: $part1")

    var next = permuted
    var totalCycleLength = 1
    while (next != initialOrder) {
        next = dance.run(next)
        totalCycleLength++
    }

    val remainderCycles = 1_000_000_000 % totalCycleLength
    val part2 = (1..remainderCycles).fold(initialOrder) { p, _ -> dance.run(p) }
        .joinToString("")
    println("part2: $part2")
}