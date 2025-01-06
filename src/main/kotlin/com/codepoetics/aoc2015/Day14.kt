package com.codepoetics.aoc2015

import com.codepoetics.aoc.inputLines
import kotlin.math.min

private val pattern = Regex("([A-Za-z]+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.")

data class Reindeer(val name: String, val flightSpeed: Int, val flightDuration: Int, val restDuration: Int) {
    fun positionAfter(seconds: Int): Int {
        val cycleLength  = flightDuration + restDuration
        val kmPerCycle = flightSpeed * flightDuration
        val completeCycles = seconds / cycleLength
        val completeCycleKm = completeCycles * kmPerCycle

        val incompleteCycleLength = seconds % cycleLength
        val secondsInFlight = min(flightDuration, incompleteCycleLength)
        val incompleteCycleKm = secondsInFlight * flightSpeed

        return completeCycleKm + incompleteCycleKm
    }
}

fun main() {
    val reindeer = inputLines("/day14.txt").map { line ->
        val (name, flightSpeedStr, flightDurationStr, restDurationStr) = pattern.matchEntire(line)!!.destructured
        Reindeer(name, flightSpeedStr.toInt(), flightDurationStr.toInt(), restDurationStr.toInt())
    }.toList()

    val part1 = reindeer.maxOf { it.positionAfter(2503) }

    println("part1: $part1")

    val pointsByReindeer = mutableMapOf<String, Int>()
    (1..2503).forEach { second ->
        val maxAfter = reindeer.maxOf { it.positionAfter(second) }
        reindeer.asSequence()
            .filter { it.positionAfter(second) == maxAfter }
            .forEach { reindeer ->
                pointsByReindeer.compute(reindeer.name) { _, v -> (v ?: 0) + 1 }
            }
    }

    val part2 = pointsByReindeer.values.max()
    println("part2: $part2")
}