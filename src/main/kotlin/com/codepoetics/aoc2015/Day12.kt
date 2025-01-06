package com.codepoetics.aoc2015

import com.codepoetics.aoc.inputLines
import java.util.*

private fun String.objectScopes(): Map<Int, Int> {
    val scopes = mutableMapOf<Int, Int>()
    val stack = ArrayDeque<Int>()
    forEachIndexed { i, c ->
        when(c) {
            '{' -> stack.push(i)
            '}' -> scopes[stack.pop()] = i
            else -> {}
        }
    }
    return scopes
}

fun main() {
    val json = inputLines("/day12.txt").first()
    val part1 = Regex("-?\\d+").findAll(json).map { it.value.toInt() }.sum()

    println("part1: $part1")

    val objectScopes = json.objectScopes()
    fun findEnclosingScope(position: Int): Pair<Int, Int> {
        return objectScopes.entries.asSequence()
            .map { (k, v) -> k to v }
            .filter { (start, end) -> position in (start..end) }
            .maxBy { (start, _) -> start }
    }
    val illegalScopes = Regex(":\"red\"").findAll(json)
        .map { findEnclosingScope(it.range.first) }
        .toList()

    val part2 = Regex("-?\\d+").findAll(json)
        .filter { match -> illegalScopes.none { (start, end) ->
            match.range.first in start..end
        } }
        .map { it.value.toInt() }
        .sum()

    println("part2: $part2")
}