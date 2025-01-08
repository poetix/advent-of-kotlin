package com.codepoetics.aoc2015

private val Int.integerDivisors: Sequence<Int> get() = (1..Math.sqrt(this.toDouble()).toInt()).asSequence()
    .filter { divisor -> this % divisor == 0 }
    .flatMap { divisor -> if (divisor == this / divisor) sequenceOf(divisor) else sequenceOf(divisor, this / divisor) }

private val Int.elfScore: Int get() = integerDivisors.map { it * 10 }.sum()
private val Int.part2ElfScore: Int get() = integerDivisors.filter { this / it <= 50 }.map { it * 11 }.sum()

fun main() {
    val target = 33100000

    val part1 = (target / 100..target).first { it.elfScore > target }
    println("part1: $part1")

    val part2 = ( target / 100 ..target).first { it.part2ElfScore > target }
    println("part2: $part2")
}

