package com.codepoetics.aoc2016

import com.codepoetics.aoc.*
import com.codepoetics.mariko.api.FromPattern
import com.codepoetics.mariko.kotlin.interpret

sealed interface PasswordOperation {

    fun invert(): PasswordOperation

    @FromPattern("rotate right (\\d+) steps?")
    data class RotateRight(val steps: Int) : PasswordOperation {
        override fun invert(): PasswordOperation = RotateLeft(steps)
    }

    @FromPattern("rotate left (\\d+) steps?")
    data class RotateLeft(val steps: Int) : PasswordOperation {
        override fun invert(): PasswordOperation = RotateRight(steps)
    }

    @FromPattern("swap position (\\d+) with position (\\d+)")
    data class SwapPositions(val p1: Int, val p2: Int) : PasswordOperation {
        override fun invert(): PasswordOperation = SwapPositions(p2, p1)
    }

    @FromPattern("swap letter ([a-z]) with letter ([a-z])")
    data class SwapLetters(val letter1: Char, val letter2: Char) : PasswordOperation {
        override fun invert(): PasswordOperation = this
    }


    @FromPattern("reverse positions (\\d+) through (\\d+)")
    data class ReversePositions(val from: Int, val to: Int) : PasswordOperation {
        override fun invert(): PasswordOperation = this
    }

    @FromPattern("move position (\\d+) to position (\\d+)")
    data class MovePosition(val from: Int, val to: Int) : PasswordOperation {
        override fun invert(): PasswordOperation = MovePosition(to, from)
    }

    @FromPattern("rotate based on position of letter ([a-z])")
    data class RotateBasedOn(val letter: Char) : PasswordOperation {
        override fun invert(): PasswordOperation = RotateBackBasedOn(letter)
    }

    @FromPattern("rotate back based on position of letter ([a-z])")
    data class RotateBackBasedOn(val letter: Char) : PasswordOperation {
        override fun invert(): PasswordOperation = RotateBasedOn(letter)
    }
}

private val inverseLookup = (0..7).associate { pos ->
    val increment = if (pos >= 4) 2 else 1
    val rotationAmount = (pos + increment) % 8
    val resultingPos = (pos + rotationAmount) % 8
    resultingPos to rotationAmount
}

private fun List<Char>.applyOperation(operation: PasswordOperation): List<Char> = when(operation) {
        is PasswordOperation.RotateRight -> indices.map { i ->
            this[(i + size - operation.steps) % size]
        }
        is PasswordOperation.RotateLeft -> indices.map { i ->
            this[(i + operation.steps) % size]
        }
        is PasswordOperation.SwapPositions -> mapIndexed { i, c ->
            when(i) {
                operation.p1 -> this[operation.p2]
                operation.p2 -> this[operation.p1]
                else -> c
            }
        }
        is PasswordOperation.SwapLetters -> map { c ->
            when(c) {
                operation.letter1 -> operation.letter2
                operation.letter2 -> operation.letter1
                else -> c
            }
        }
        is PasswordOperation.ReversePositions -> mapIndexed { i, c ->
            if (i in (operation.from)..(operation.to)) this[operation.from + (operation.to - i)]
            else c
        }
        is PasswordOperation.MovePosition ->
            if (operation.from > operation.to) mapIndexed { i, c ->
                when {
                    i == operation.to -> this[operation.from]
                    i > operation.to && i <= operation.from -> this[i - 1]
                    else -> c
                }
            } else mapIndexed { i, c ->
                when {
                    i == operation.to -> this[operation.from]
                    i >= operation.from && i < operation.to -> this[i + 1]
                    else -> c
                }
            }
        is PasswordOperation.RotateBasedOn -> {
            val indexOfChar = indexOf(operation.letter)
            val increment = if (indexOfChar >= 4) 2 else 1
            val rotationAmount = (indexOfChar + increment) % size
            applyOperation(PasswordOperation.RotateRight(rotationAmount))
        }
        is PasswordOperation.RotateBackBasedOn -> {
            val indexOfChar = indexOf(operation.letter)
            val rotateBackAmount = inverseLookup[indexOfChar]!!
            applyOperation(PasswordOperation.RotateLeft(rotateBackAmount))
        }
    }

fun main() {
    val instructions = inputLines("/2016/day21.txt").interpret<PasswordOperation>().toList()

    val input = "abcdefgh".toCharArray().toList()
    val part1 = instructions.fold(input) { pwd, i ->
        pwd.applyOperation(i)
    }.joinToString("")

    val output = "fbgdceah".toCharArray().toList()
    val part2 = instructions.reversed().asSequence().map { it.invert() }.fold(output) { pwd, i ->
        pwd.applyOperation(i)
    }.joinToString("")

    println("part1: $part1")
    println("part2: $part2")
}