package com.codepoetics.aoc2015

import com.codepoetics.aoc.inputLines

private fun String.indicesOf(substr: String): Sequence<Int> = sequence {
    var nextIndex = indexOf(substr, 0)
    while (nextIndex != -1) {
        yield(nextIndex)
        nextIndex = indexOf(substr, nextIndex + 1)
    }
}

private fun String.replaceAt(index: Int, length: Int, replacement: String): String =
    substring(0, index) + replacement + substring(index + length)

fun main() {
    val iter = inputLines("/day19.txt").iterator()
    val transforms = mutableListOf<Pair<String, String>>()
    while (iter.hasNext()) {
        val next = iter.next()
        if (next.isEmpty()) break

        val (from, to) = next.split(" => ")
        transforms.add(from to to)
    }
    val medicine = iter.next()

    val transformMap = transforms.groupBy(Pair<String, String>::first, Pair<String, String>::second)

    val part1 = transformMap.asSequence().flatMap { (key, transforms) ->
        medicine.indicesOf(key).flatMap { index ->
            transforms.map { transform ->
                medicine.replaceAt(index, key.length, transform)
            }
        }
    }.distinct().count()

    println("part1: $part1")

    val longestFirst = transforms.asSequence()
        .sortedByDescending { (_, long) -> long.length }
        .toList()

    fun shortestToE(current: String): Int {
        val reduction = longestFirst.asSequence()
            .mapNotNull { (short, long) ->
            current.indexOf(long).let { index ->
                if (index > -1) {
                    current.replaceAt(index, long.length, short)
                } else null
            }
        }.first()

        return if (reduction == "e") 1 else shortestToE(reduction) + 1
    }

    val part2 = shortestToE(medicine)
    println("part2: $part2")
}