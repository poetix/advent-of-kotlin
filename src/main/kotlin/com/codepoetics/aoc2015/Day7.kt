package com.codepoetics.aoc2015

import com.codepoetics.aoc.inputLines

sealed interface Gate {

    companion object {
        fun parse(line: String): Gate {
            val (source, output) = line.split(" -> ")
            if (source.startsWith("NOT")) return Not(source.substring(4), output)
            if (source.contains("AND")) {
                val (lhs, rhs) = source.split(" AND ")
                return And(lhs, rhs, output)
            }
            if (source.contains("OR")) {
                val (lhs, rhs) = source.split(" OR ")
                return Or(lhs, rhs, output)
            }
            if (source.contains("LSHIFT")) {
                val (lhs, rhs) = source.split(" LSHIFT ")
                return LShift(lhs, rhs.toInt(), output)
            }
            if (source.contains("RSHIFT")) {
                val (lhs, rhs) = source.split(" RSHIFT ")
                return RShift(lhs, rhs.toInt(), output)
            }
            return Relay(source, output)
        }
    }

    val output: String
    fun calculate(calculated: MutableMap<String, Int>, sources: Map<String, Gate>): Int
    fun digitOrCalculate(expr: String, calculated: MutableMap<String, Int>, sources: Map<String, Gate>): Int =
        if (expr.first().isDigit()) expr.toInt() else calculated[expr]
            ?: sources[expr]!!.calculate(calculated, sources).also { calculated[expr] = it }

    data class Relay(val lhs: String, override val output: String) : Gate {
        override fun calculate(calculated: MutableMap<String, Int>, sources: Map<String, Gate>): Int {
            return digitOrCalculate(lhs, calculated, sources)
        }
    }

    data class Or(val lhs: String, val rhs: String, override val output: String) : Gate {
        override fun calculate(calculated: MutableMap<String, Int>, sources: Map<String, Gate>): Int {
            val lhsVal = digitOrCalculate(lhs, calculated, sources)
            val rhsVal =  digitOrCalculate(rhs, calculated, sources)
            return lhsVal or rhsVal
        }
    }

    data class And(val lhs: String, val rhs: String, override val output: String) : Gate {
        override fun calculate(calculated: MutableMap<String, Int>, sources: Map<String, Gate>): Int {
            val lhsVal = digitOrCalculate(lhs, calculated, sources)
            val rhsVal =  digitOrCalculate(rhs, calculated, sources)
            return lhsVal and rhsVal
        }
    }

    data class LShift(val lhs: String, val rhs: Int, override val output: String) : Gate {
        override fun calculate(calculated: MutableMap<String, Int>, sources: Map<String, Gate>): Int {
            return (digitOrCalculate(lhs, calculated, sources) shl rhs) and 65535
        }
    }

    data class RShift(val lhs: String, val rhs: Int, override val output: String) : Gate {
        override fun calculate(calculated: MutableMap<String, Int>, sources: Map<String, Gate>): Int {
            return digitOrCalculate(lhs, calculated, sources) shr rhs
        }
    }

    data class Not(val lhs: String, override val output: String) : Gate {
        override fun calculate(calculated: MutableMap<String, Int>, sources: Map<String, Gate>): Int {
            return digitOrCalculate(lhs, calculated, sources) xor 65535
        }
    }
}

fun main() {
    val gates = inputLines("/day7.txt").map(Gate::parse).associateBy(Gate::output)
    val calculated = mutableMapOf<String, Int>()
    val part1 = gates["a"]!!.calculate(calculated, gates)

    val calculatedPart2 = mutableMapOf<String, Int>().apply { this["b"] = part1 }
    val part2 = gates["a"]!!.calculate(calculatedPart2, gates)

    println("part1: $part1")
    println("part2: $part2")
}