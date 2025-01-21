package com.codepoetics.aoc2016

import com.codepoetics.aoc.inputLines
import kotlin.collections.ArrayDeque


sealed interface Destination {

    companion object {
        fun of(type: String, index: Int) = if (type=="bot") Bot(index) else Output(index)
    }

    data class Output(val index: Int) : Destination
    data class Bot(val index: Int) : Destination
}

class BotState {

    companion object {
        private val assignPattern = Regex("value (\\d+) goes to bot (\\d+)")
        private val splitPattern = Regex("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)")
    }

    private val lowByBot = mutableMapOf<Int, Int>()
    private val highByBot = mutableMapOf<Int, Int>()
    private val completeBots = mutableSetOf<Int>()
    private val splits = mutableMapOf<Int, Pair<Destination, Destination>>()
    private val outputs = mutableMapOf<Int, Int>()

    fun initialise(data: Sequence<String>) {
        data.forEach { line ->
            assignPattern.matchEntire(line)?.also { match ->
                val (valueStr, botStr) = match.destructured
                giveToBot(botStr.toInt(), valueStr.toInt())
            }

            splitPattern.matchEntire(line)?.also { match ->
                val (bot, lowDestType, lowDestIdx, highDestType, highDestIdx) = match.destructured
                splits[bot.toInt()] =
                    Destination.of(lowDestType, lowDestIdx.toInt()) to
                        Destination.of(highDestType, highDestIdx.toInt())
            }
        }
        println(splits)
    }

    fun distributeChips(): Pair<Int, Int> {
        val queue = ArrayDeque<Int>()
        queue.addAll(completeBots)

        var part1 = 0
        while (queue.isNotEmpty()) {
            processBot(queue.removeFirst(), queue)?.also { part1 = it }
        }

        val part2 = outputs[0]!! * outputs[1]!! * outputs[2]!!
        return part1 to part2
    }

    private fun processBot(bot: Int, queue: ArrayDeque<Int>): Int? {
        val (low, high) = splits[bot]!!
        val lowVal = lowByBot[bot]!!
        val highVal = highByBot[bot]!!

        distributeChip(low, lowVal, queue)
        distributeChip(high, highVal, queue)

        return if (lowVal == 17 && highVal == 61) bot else null
    }

    private fun distributeChip(destination: Destination, chipValue: Int, queue: ArrayDeque<Int>) {
        when (destination) {
            is Destination.Bot -> if (giveToBot(destination.index, chipValue)) queue.add(destination.index)
            is Destination.Output -> outputs[destination.index] = chipValue
        }
    }

    private fun giveToBot(bot: Int, value: Int): Boolean {
        val held = lowByBot[bot]

        if (held == null) {
            lowByBot[bot] = value
            return false
        }

        if (held < value) highByBot[bot] = value
        else {
            highByBot[bot] = held
            lowByBot[bot] = value
        }

        completeBots.add(bot)
        return true
    }
}

fun main() {
    val (part1, part2) = BotState()
        .apply { initialise(inputLines("/2016/day10.txt")) }
        .distributeChips()

    println("part1: $part1")
    println("part2: $part2")
}