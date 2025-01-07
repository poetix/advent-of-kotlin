package com.codepoetics.aoc2015

import com.codepoetics.aoc.Lst
import com.codepoetics.aoc.inputLines
import com.codepoetics.aoc.toLst
import kotlin.math.min

fun countCombinations(sizes: Lst<Int>, target: Int, allowance: Int): Int =
    if (sizes.isEmpty() || allowance == 0) 0 else (sizes as Lst.Cons<Int>).run {
        if (head > target) 0
        else if (head == target) 1 + countCombinations(tail, target, allowance)
        else countCombinations(tail, target - head, allowance - 1) +
                countCombinations(tail, target, allowance)
    }

fun minContainers(sizes: Lst<Int>, target: Int): Int =
    if (sizes.isEmpty()) Int.MAX_VALUE - 1 else (sizes as Lst.Cons<Int>).run {
        if (head > target) Int.MAX_VALUE - 1
        else if (head == target) 1
        else min(minContainers(tail, target - head) + 1, minContainers(tail, target))
    }

fun main() {
    val sizes = inputLines("/day17.txt").map { it.toInt() }.sortedDescending().toLst()

    val part1 = countCombinations(sizes, 150, sizes.length)
    println("part1: $part1")

    val min = minContainers(sizes, 150)
    val part2 = countCombinations(sizes, 150, min)

    println("part2: $part2")
}