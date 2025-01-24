package com.codepoetics.aoc2017

import com.codepoetics.aoc.inputLines
import com.codepoetics.mariko.api.FromPattern
import com.codepoetics.mariko.kotlin.interpret

sealed interface Program {

    val name: String
    val weight: Int

    @FromPattern("([a-z]+) \\((\\d+)\\)")
    data class Leaf(override val name: String, override val weight: Int) : Program

    @FromPattern("([a-z]+) \\((\\d+)\\) \\-> (.*)")
    data class Branch(override val name: String, override val weight: Int, val holding: List<String>) : Program
}

fun main() {
    val input = inputLines("/2017/day7.txt").interpret<Program>().associateBy(Program::name)

    val heldByOthers = input.values.asSequence()
        .filterIsInstance<Program.Branch>()
        .flatMap { it.holding.asSequence() }
        .toSet()

    val base = input.values.first { it.name !in heldByOthers }
    val part1 = base.name
    println("part1: $part1")

    val totalWeights = mutableMapOf<String, Int>()

    fun computeTotalWeight(name: String): Int =
        totalWeights.getOrPut(name) {
            when(val program = input[name]!!) {
                is Program.Leaf -> program.weight
                is Program.Branch -> program.weight + program.holding.sumOf { computeTotalWeight(it) }
            }
        }

    fun isBalanced(name: String): Boolean =
        when (val program = input[name]!!) {
            is Program.Leaf -> true
            is Program.Branch -> program.holding.distinctBy { computeTotalWeight(it) }.size == 1
        }

    val unbalanced = input.values.filterIsInstance<Program.Branch>().first { program ->
        !isBalanced(program.name) && program.holding.all { isBalanced(it) }
    }

    val weights = unbalanced.holding.groupBy { computeTotalWeight(it) }
    val correctWeight = weights.entries.first { (_, count) -> count.size > 1 }.key
    val incorrectProgram = input[weights.entries.first { (_, count) -> count.size == 1}.value.first()]!!

    val part2 = incorrectProgram.weight + correctWeight - computeTotalWeight(incorrectProgram.name)
    println("part2: $part2")
}