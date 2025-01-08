package com.codepoetics.aoc2015

import com.codepoetics.aoc.Lst
import com.codepoetics.aoc.inputLines
import com.codepoetics.aoc.toLst

private val pattern = Regex("([A-Za-z]+) would (gain|lose) (\\d+) happiness units by sitting next to ([A-Za-z]+).")

data class Preference(val person: String, val other: String, val happiness: Int)

fun main() {
    val preferences = inputLines("/day13.txt").map {
        val (person, gainOrLoss, happinessStr, other) = pattern.matchEntire(it)!!.destructured
        val happiness = if (gainOrLoss == "lose") -(happinessStr.toInt()) else happinessStr.toInt()
        Preference(person, other, happiness)
    }.toList()

    val part1 = optimalSeatingValue(preferences)

    val part2Preferences = preferences + preferences.asSequence().map(Preference::person).distinct().flatMap { person ->
        sequenceOf(
            Preference(person, "me", 0),
            Preference("me", person, 0))
    }.toList()

    val part2 = optimalSeatingValue(part2Preferences)

    println("part1: $part1")
    println("part2: $part2")
}

private fun optimalSeatingValue(preferences: List<Preference>): Int {
    val happinessByPair = preferences.associateBy(
        { (person, other, _) -> person to other },
        Preference::happiness
    )

    fun happinessForPair(a: String, b: String): Int = happinessByPair[a to b]!! + happinessByPair[b to a]!!
    val people = preferences.asSequence().map(Preference::person).distinct().toLst()

    return people.permutations().maxOf { seating ->
        seating.asSequence().windowed(2).sumOf { (a, b) ->
            happinessForPair(a, b)
        } + happinessForPair(seating.first, seating.last)
    }
}

