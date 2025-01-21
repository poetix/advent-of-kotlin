package com.codepoetics.aoc2016

fun main() {
    val part1 = winnerStealingFromNextNeighbour(3014603)
    println("part1: $part1")

    val part2 = winnerStealingFromOpposite(3014603)
    println("part2: $part2")
}

private fun winnerStealingFromNextNeighbour(elfCount: Int): Int {
    val nextElfByElf = (0..<elfCount).associateWith { (it + 1) % elfCount }.toMutableMap()
    var currentElf = 0
    while (nextElfByElf.size > 1) {
        val nextElf = nextElfByElf[currentElf]!!
        val nextNextElf = nextElfByElf[nextElf]!!
        nextElfByElf[currentElf] = nextNextElf
        nextElfByElf.remove(nextElf)
        currentElf = nextNextElf
    }
    return currentElf + 1
}

private fun winnerStealingFromOpposite(elfCount: Int): Int {
    val neighbourElvesByElf = (0..<elfCount).associateWith {
        ((it + elfCount - 1) % elfCount) to (it + 1) % elfCount
    }.toMutableMap()

    var currentElfId = 0
    var currentElfPos = 0
    var oppositeElfId = elfCount / 2
    var oppositeElfPos = oppositeElfId

    while (neighbourElvesByElf.size > 1) {
        val modulus = neighbourElvesByElf.size
        currentElfPos %= modulus
        oppositeElfPos %= modulus

        // Seek the opposite elf
        val desiredOppositePos = (currentElfPos + (modulus / 2)) % modulus
        if (oppositeElfPos != desiredOppositePos) {
            if ((oppositeElfPos + modulus - 1) % modulus == desiredOppositePos) {
                oppositeElfId = neighbourElvesByElf[oppositeElfId]!!.first
                oppositeElfPos = (oppositeElfPos + modulus - 1) % modulus
            } else {
                oppositeElfId = neighbourElvesByElf[oppositeElfId]!!.second
                oppositeElfPos = (oppositeElfPos + 1) % modulus
            }
        }

        // Remove and relink neighbours of opposite.
        val (prevOppositeElfId, nextOppositeElfId) = neighbourElvesByElf[oppositeElfId]!!

        val (prevPrevOppositeElfId, _) = neighbourElvesByElf[prevOppositeElfId]!!
        val (_, nextNextOppositeElfId) = neighbourElvesByElf[nextOppositeElfId]!!

        neighbourElvesByElf.remove(oppositeElfId)
        neighbourElvesByElf[prevOppositeElfId] = prevPrevOppositeElfId to nextOppositeElfId
        neighbourElvesByElf[nextOppositeElfId] = prevOppositeElfId to nextNextOppositeElfId

        if (neighbourElvesByElf.size > 1)  {
            // Move to the next elf
            currentElfId = neighbourElvesByElf[currentElfId]!!.second
            currentElfPos = if (oppositeElfPos < currentElfPos) currentElfPos else (currentElfPos + 1)

            // Move ptr to next opposite, which takes the current opposite's position
            oppositeElfId = nextOppositeElfId
        }
    }
    return currentElfId + 1
}