package com.codepoetics.aoc2017

import com.codepoetics.aoc.inputLines
import com.codepoetics.mariko.api.FromPattern
import com.codepoetics.mariko.kotlin.inNewContext

enum class IncOrDec {
    INC,
    DEC
}

enum class Comparison {
    GT,
    LT,
    GTE,
    LTE,
    EQ,
    NEQ
}

@FromPattern("([a-z]+) (inc|dec) (-?\\d+) if ([a-z]+) ([^\\s]+) (-?\\d+)")
data class RegisterInstruction(
    val register: String,
    val incOrDec: IncOrDec,
    val amount: Int,
    val conditionLhs: String,
    val compare: Comparison,
    val conditionRhs: Int) {

    fun execute(registers: MutableMap<String, Int>): Int {
        val conditionLhsVal = registers[conditionLhs] ?: 0
        val registerVal = registers[register] ?: 0
        val conditionMet = when(compare) {
            Comparison.GT -> conditionLhsVal > conditionRhs
            Comparison.GTE -> conditionLhsVal >= conditionRhs
            Comparison.LT -> conditionLhsVal < conditionRhs
            Comparison.LTE -> conditionLhsVal <= conditionRhs
            Comparison.EQ -> conditionLhsVal == conditionRhs
            Comparison.NEQ -> conditionLhsVal != conditionRhs
        }
        val newValue = if (incOrDec == IncOrDec.INC) registerVal + amount else registerVal - amount
        return if (conditionMet) newValue.also { registers[register] = it } else 0
    }
}

fun main() {
    val instructions = inNewContext {
        add<Comparison> { when(it) {
                "<" -> Comparison.LT
                "<=" -> Comparison.LTE
                ">" -> Comparison.GT
                ">=" -> Comparison.GTE
                "==" -> Comparison.EQ
                "!=" -> Comparison.NEQ
                else -> error("$it not recognised")
            }
        }
        inputLines("/2017/day8.txt").interpret<RegisterInstruction>()
    }

    val registers = mutableMapOf<String, Int>()
    val part2 = instructions.maxOf { it.execute(registers) }

    val part1 = registers.values.max()
    println("part1: $part1")
    println("part2: $part2")
}