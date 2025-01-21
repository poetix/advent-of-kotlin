package com.codepoetics.aoc2016

import com.codepoetics.aoc.decrement
import com.codepoetics.aoc.increment
import java.util.*

data class ItemState(
    val elevatorFloor: Int,
    val chipGeneratorPairs: Map<Pair<Int, Int>, Int>) {

    val isValid: Boolean get() =
        chipGeneratorPairs.keys.all { (chip, generator) ->
            chip == generator || chipGeneratorPairs.none { (_, generator) -> generator == chip }
        }

    val isFinal: Boolean = chipGeneratorPairs.keys.all { (chip, generator) ->
        chip == 3 && generator == 3
    }

    private fun update(newFloor: Int, oldPair: Pair<Int, Int>, newPair: Pair<Int, Int>): ItemState =
        ItemState(newFloor, chipGeneratorPairs.toMutableMap().apply {
            decrement(oldPair)
            increment(newPair)
        })

    private fun movingTwo(fromFloor: Int, toFloor: Int): Sequence<ItemState> = sequence {
        for ((chip, generator) in chipGeneratorPairs.keys) {
            if (chip == fromFloor) yieldAll(update(toFloor, (chip to generator), (toFloor to generator))
                .maybeMovingOneMore(fromFloor, toFloor))
            if (generator == fromFloor) yieldAll(update(toFloor, (chip to generator), (chip to toFloor))
                .maybeMovingOneMore(fromFloor, toFloor))
        }
    }

    private fun maybeMovingOneMore(fromFloor: Int, toFloor: Int): Sequence<ItemState> = sequence {
        for ((chip, generator) in chipGeneratorPairs.keys) {
            if (chip == fromFloor) yield(update(toFloor, (chip to  generator), (toFloor to generator)))
            if (generator == fromFloor) yield(update(toFloor, (chip to generator), (chip to toFloor)))
        }

        yield(this@ItemState)
    }

    fun nextStates(): Sequence<ItemState> = sequence {
        if (elevatorFloor > 0) yieldAll(movingTwo(elevatorFloor, elevatorFloor - 1))
        if (elevatorFloor < 3) yieldAll(movingTwo(elevatorFloor, elevatorFloor + 1))
    }.filter { it.isValid }
}

fun main() {
    val initialState = ItemState(0,
    mapOf(
        (0 to 0) to 1,
        (0 to 1) to 2,
        (2 to 2) to 2
    ))

    val part1 = countMovesToFinalState(initialState)
    println("part1: $part1")

    val initialState2 = ItemState(0,
        mapOf(
            (0 to 0) to 3,
            (0 to 1) to 2,
            (2 to 2) to 2
        )   )

    val part2 = countMovesToFinalState(initialState2)
    println("part2: $part2")
}

private fun countMovesToFinalState(initialState: ItemState): Int {
    val visited = mutableSetOf<ItemState>()
    val queue = ArrayDeque<Pair<Int, ItemState>>()
    queue.add(0 to initialState)
    visited.add(initialState)

    while (!queue.isEmpty()) {
        val (count, next) = queue.remove()

        if (next.isFinal) {
            return count
        }

        for (nextState in next.nextStates()) {
            if (nextState !in visited) {
                visited.add(nextState)
                queue.add(count + 1 to nextState)
            }
        }
    }
    return Int.MAX_VALUE
}