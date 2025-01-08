package com.codepoetics.aoc2015

import kotlin.math.max

data class Item(val name: String, val cost: Int, val dmg: Int, val arm: Int)

val weapons = listOf(
    Item("Dagger", 8, 4, 0),
    Item("Shortsword", 10, 5, 0),
    Item("Warhammer", 25, 6, 0),
    Item("Longsword", 40, 7, 0),
    Item("Greataxe", 74, 8, 0)
)

val armor = listOf(
    Item("Leather", 13, 0, 1),
    Item("Chainmail", 31, 0, 2),
    Item("Splintmail", 53, 0, 3),
    Item("Bandedmail", 75, 0, 4),
    Item("Platemail", 102, 0, 5),
    Item("None", 0, 0, 0)
)

val rings = listOf(
    Item("Damage + 1", 25, 1, 0),
    Item("Damage + 2", 50, 2, 0),
    Item("Damage + 3", 100, 3, 0),
    Item("Defence + 1", 20, 0, 1),
    Item("Defence + 2", 40, 0, 2),
    Item("Defence + 3", 80, 0, 3),
    Item("None", 0, 0, 0)
)

data class Build(val weapon: Item, val armor: Item, val leftRing: Item, val rightRing: Item) {
    val cost: Int get() = weapon.cost + armor.cost + leftRing.cost + rightRing.cost
    val dmg: Int get() = weapon.dmg + leftRing.dmg + rightRing.dmg
    val arm: Int get() = armor.arm + leftRing.arm + rightRing.arm
}

fun builds(): Sequence<Build> = weapons.asSequence().flatMap { weapon ->
    armor.asSequence().flatMap { armor ->
        rings.asSequence().flatMap { leftRing ->
            rings.asSequence().filter { it != leftRing || leftRing.cost == 0 }.map { rightRing ->
                Build(weapon, armor, leftRing, rightRing)
            }
        }
    }
}

fun playerWins(build: Build, initialPlayerHp: Int, initialBossHp: Int, bossDmg: Int, bossArm: Int): Boolean {
    val playerDmgPerRound = max(1, build.dmg - bossArm)
    val bossDmgPerRound = max(1, bossDmg - build.arm)

    val playerSurvivableRounds = (initialPlayerHp / bossDmgPerRound) + (if (initialPlayerHp % bossDmgPerRound > 0) 1 else 0)
    val bossSurvivableRounds = (initialBossHp / playerDmgPerRound) + (if (initialBossHp % playerDmgPerRound > 0) 1 else 0)

    return playerSurvivableRounds >= bossSurvivableRounds
}

fun main() {
    val part1 = builds().filter { playerWins(it, 100, 104, 8, 1) }
        .minOf(Build::cost)

    val part2 = builds().filter { !playerWins(it, 100, 104, 8, 1) }
        .maxOf(Build::cost)

    println("part1: $part1")
    println("part2: $part2")
}