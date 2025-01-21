package com.codepoetics.aoc2016

import com.codepoetics.aoc.inputLines
import com.codepoetics.mariko.api.FromPattern
import com.codepoetics.mariko.kotlin.interpret

sealed interface Operand {

    fun value(registers: Map<Char, Int>): Int

    @FromPattern("([a-z])")
    data class Register(val name: Char) : Operand {
        override fun value(registers: Map<Char, Int>) = registers[name]!!
    }

    @FromPattern("(-?\\d+)")
    data class Value(val value: Int) : Operand {
        override fun value(registers: Map<Char, Int>): Int = value
    }
}

sealed interface Opcode {

    fun toggled(): Opcode

    @FromPattern("cpy ([^\\s]+) ([^\\s]+)")
    data class Copy(val lhs: Operand, val rhs: Operand) : Opcode {
        override fun toggled(): Opcode = Jnz(lhs, rhs)
    }

    @FromPattern("dec ([a-z]+)")
    data class Dec(val register: Char) : Opcode {
        override fun toggled(): Opcode = Inc(register)
    }

    @FromPattern("inc ([a-z]+)")
    data class Inc(val register: Char) : Opcode {
        override fun toggled(): Opcode = Dec(register)
    }

    @FromPattern("jnz ([^\\s]+) ([^\\s]+)")
    data class Jnz(val lhs: Operand, val rhs: Operand) : Opcode {
        override fun toggled(): Opcode = Copy(lhs, rhs)
    }

    @FromPattern("tgl ([a-z]+)")
    data class Tgl(val register: Char) : Opcode {
        override fun toggled(): Opcode = Inc(register)
    }

    @FromPattern("out ([a-z])")
    data class Out(val register: Char) : Opcode {
        override fun toggled(): Opcode = Inc(register)
    }
}

data class ProgramState(val ptr: Int, val registers: Map<Char, Int>, val program: List<Opcode>, val output: String) {
    fun step(optimise: Boolean): ProgramState =
        if (ptr == 4 && optimise) {
            val b = registers['b']!!
            val a = registers['a']!! + b * registers['d']!!
            ProgramState(10, mapOf('a' to a, 'b' to b, 'c' to 0, 'd' to 0), program, "")
        } else
        when (val opcode = program[ptr]) {
            is Opcode.Copy -> if (opcode.rhs is Operand.Register) setRegister(
                opcode.rhs.name, opcode.lhs.value(registers)
            ) else skip()
            is Opcode.Inc -> setRegister(opcode.register, registers[opcode.register]!! + 1)
            is Opcode.Dec -> setRegister(opcode.register, registers[opcode.register]!! - 1)
            is Opcode.Jnz -> if (opcode.lhs.value(registers) != 0) copy(ptr = ptr + opcode.rhs.value(registers))
                else skip()
            is Opcode.Tgl -> {
                val target = ptr + registers[opcode.register]!!
                if (target in program.indices) copy(
                    ptr = ptr + 1,
                    program = program.mapIndexed { i, o ->
                        if (i == target) o.toggled() else o
                    })
                else skip()
            }
            is Opcode.Out -> copy(ptr = ptr + 1, output = output + registers[opcode.register]!!)
        }

    private fun skip(): ProgramState =  copy(ptr = ptr + 1)

    private fun setRegister(name: Char, value: Int): ProgramState =
        copy(ptr = ptr + 1, registers = registers + (name to value ))
}

fun main() {
    val program = inputLines("/2016/day23.txt").interpret<Opcode>().toList()
    var state = ProgramState(0, mapOf('a' to 7, 'b' to 0, 'c' to 0, 'd' to 0), program, "")

    while (state.ptr in program.indices) {
        state = state.step(optimise = true)
    }
    val part1 = state.registers['a']!!
    println("part1: $part1")

    state = ProgramState(0, mapOf('a' to 12, 'b' to 0, 'c' to 0, 'd' to 0), program, "")
    program.forEachIndexed { i, opcode -> println("$i: $opcode")}

    while (state.ptr in program.indices) {
        state = state.step(optimise = true)
    }

    val part2 = state.registers['a']!!
    println("part2: $part2")
}