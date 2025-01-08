package com.codepoetics.aoc2015

import java.util.PriorityQueue
import kotlin.math.max

data class GameState(val playerHp: Int, val bossHp: Int, val mana: Int, val shieldCount: Int, val poisonCount: Int, val rechargeCount: Int, val hardMode: Boolean) {

    fun doEffects(): GameState {
        val poisonDmg = if (poisonCount > 0) 3 else 0
        val rechargeAmount = if (rechargeCount > 0) 101 else 0

        return copy(
            bossHp = bossHp - poisonDmg,
            mana = mana + rechargeAmount,
            shieldCount = max(0, shieldCount - 1),
            poisonCount = max(0, poisonCount - 1),
            rechargeCount = max(0, rechargeCount - 1)
        )
    }

    fun bossTurn(): GameState {
        if (bossHp < 1) return this

        val bossDmg = if (shieldCount > 0) 1 else 8

        return copy(playerHp = playerHp - bossDmg)
    }

    fun magicMissile(): GameState {
        return copy(
            bossHp = bossHp - 4,
            mana = mana - 53
        )
    }

    fun drain(): GameState {
        return copy(
            bossHp = bossHp - 2,
            playerHp = playerHp + 2,
            mana = mana -  73
        )
    }

    fun shield(): GameState {
        return copy(
            mana = mana - 113,
            shieldCount = 6
        )
    }

    fun poison(): GameState {
        return copy(
            mana = mana - 173,
            poisonCount = 6
        )
    }

    fun recharge(): GameState {
        return copy(
            mana = mana - 229,
            rechargeCount = 5
        )
    }

    fun playerTurn(): Sequence<Pair<Int, GameState>> = sequence {
        if (playerHp < 1 || mana < 53) return@sequence

        yield(53 to magicMissile())
        if (mana >= 73) yield (73 to drain())
        if (shieldCount == 0 && mana >= 113) yield(113 to shield())
        if (poisonCount == 0 && mana >= 173) yield(173 to poison())
        if (rechargeCount == 0 && mana >= 229) yield(229 to recharge())
    }

    fun applyHardMode(): GameState =
        if (hardMode) copy(playerHp = playerHp - 1) else this

    fun nextTurn(): Sequence<Pair<Int, GameState>> = applyHardMode()
        .doEffects()
        .playerTurn().map { (cost, state) ->
        if (bossHp > 0 && playerHp > 0) cost to state.doEffects().bossTurn() else cost to state
    }
}

fun findCheapestWin(hardMode: Boolean): Int {
    val initialState = GameState(50, 55, 500, 0, 0, 0, hardMode)
    val queue = PriorityQueue(compareBy<Pair<Int, GameState>> { it.first })
    queue.add(0 to initialState)

    while (queue.isNotEmpty()) {
        val (cost, state) = queue.remove()
        if (state.bossHp <= 0) return cost
        if (state.playerHp > 0 && state.mana >= 53) state.nextTurn().forEach { (turnCost, newState) ->
            queue.add(cost + turnCost to newState)
        }
    }

    error("There is no way to win!!!")
}

fun main() {
    val part1 = findCheapestWin(false)
    val part2 = findCheapestWin(true)

    println("part1: $part1")
    println("part2: $part2")
}