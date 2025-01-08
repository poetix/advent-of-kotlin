package com.codepoetics.aoc2015

import com.codepoetics.aoc.Lst
import com.codepoetics.aoc.cons
import com.codepoetics.aoc.inputLines
import com.codepoetics.aoc.toLst
import java.util.*
import kotlin.collections.ArrayDeque

fun main() {
    val weights = inputLines("/day24.txt").map(String::toInt).sorted().toLst()

    val part1 = smallestEntanglementOfFirstPartition(weights, 3)
    val part2 = smallestEntanglementOfFirstPartition(weights, 4)

    println("part1: $part1")
    println("part2: $part2")
}

private fun smallestEntanglementOfFirstPartition(weights: Lst<Int>, groups: Int): Long {
    val totalWeight = weights.sum()
    val groupWeight = totalWeight / groups

    return partitions(groupWeight, weights).first().entanglement
}

data class PartitionSearch(val weight: Int, val partition: Lst<Int>, val remainder: Lst<Int>) {
    val packageCount: Int get() = partition.length
    val entanglement: Long get() = partition.map(Int::toLong).reduce(Long::times)
}

private fun partitions(targetWeight: Int, weights: Lst<Int>): Sequence<PartitionSearch> = sequence {
    val queue = PriorityQueue(compareBy(PartitionSearch::packageCount)
        .then(compareBy(PartitionSearch::entanglement))
    )

    weights.choices().forEach { (choice, remainder) ->
        queue.add(PartitionSearch(choice, Lst.of(choice), remainder))
    }

    while (queue.isNotEmpty()) {
        val search = queue.remove()
        val (weight, partition, remainder) = search

        if (weight == targetWeight) yield(search)

        for ((choice, newRemainder) in remainder.conses()) {
            val newWeight = choice + weight
            if (newWeight > targetWeight || remainder.sum() < targetWeight) continue
            queue.add(PartitionSearch(newWeight, choice cons partition, newRemainder))
        }
    }
}