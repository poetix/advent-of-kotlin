package com.codepoetics.aoc2016

import com.codepoetics.aoc.inputLines
import kotlin.streams.asSequence

data class Room(val name: List<String>, val sectorId: Int, val checksum: String) {
    fun isReal(): Boolean {
        val counts = name.flatMap { it.asSequence() }.groupingBy { it }.eachCount()
        val expectedChecksum = counts.asSequence()
            .sortedWith(compareBy<Map.Entry<Char, Int>> { (_, v) -> v }.reversed()
                .then(compareBy { (k, _) -> k }))
            .map { (k, _) -> k }
            .take(5).joinToString("")

        return checksum == expectedChecksum
    }

    fun realName(): String = name.joinToString(" ") { section ->
        String(section.chars().asSequence().map { c ->
            ('a'.code + ((c + sectorId - 'a'.code) % 26)).toChar()
        }.toList().toCharArray())
    }
}

fun main() {
    val rooms = inputLines("/2016/day4.txt").map { line ->
        val segments = line.split("-")
        val name = segments.slice(0..<segments.lastIndex)
        val (sectorIdStr, checksum) = segments.last().let { it.substring(0, it.length - 1).split("[") }
        Room(name, sectorIdStr.toInt(), checksum)
    }.toList()

    val part1 = rooms.filter { it.isReal() }.sumOf { it.sectorId }
    println("part1: $part1")

    val part2 = rooms.filter { it.isReal() }.filter { it.realName() == "northpole object storage" }.first().sectorId
    println("part2: $part2")
}