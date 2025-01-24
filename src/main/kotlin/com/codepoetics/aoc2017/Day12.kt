package com.codepoetics.aoc2017

import com.codepoetics.aoc.DisjointSet
import com.codepoetics.aoc.inputLines
import com.codepoetics.mariko.api.FromPattern
import com.codepoetics.mariko.kotlin.interpret

@FromPattern("(\\d+) <-> (.*)")
data class PipeSet(val start: Int, val ends: List<Int>)

fun main() {
    val pipeSets = inputLines("/2017/day12.txt").interpret<PipeSet>()

    val disjointSet = DisjointSet<Int>().apply {
        pipeSets.forEach { ps -> ps.ends.forEach {
            add(ps.start)
            add(it)
            connect(ps.start, it) }
        }
    }

    val part1 = disjointSet.groupContaining(0).count()
    val part2 = disjointSet.groupCount()
    println("part1: $part1")
    println("part2: $part2")
}
