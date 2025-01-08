package com.codepoetics.aoc2015

import com.codepoetics.aoc.inputLines

sealed interface Code {

    companion object {
        fun interpret(line: String): Code =
            when (line.substring(0, 3)) {
                "jio" -> JmpIfAOne(line.split(", ")[1].toInt())
                "jie" -> JmpIfAEven(line.split(", ")[1].toInt())
                "jmp" -> Jmp(line.split(" ")[1].toInt())
                "inc" -> if (line.endsWith("a")) IncA else IncB
                "tpl" -> TripleA
                "hlf" -> HalfA
                else -> error("Unrecognised opcode $line")
            }
    }

    data object IncA : Code
    data object TripleA : Code
    data object HalfA : Code
    data object IncB: Code
    data class Jmp(val offset: Int) : Code
    data class JmpIfAEven(val offset: Int) : Code
    data class JmpIfAOne(val offset: Int) : Code
}

data class MachineState(val ptr: Int, val a: Int, val b: Int) {
    fun runCode(code: Code): MachineState = when(code) {
        is Code.Jmp -> copy(ptr = ptr + code.offset)
        is Code.JmpIfAEven -> if (a % 2 == 0) copy(ptr = ptr + code.offset) else advance()
        is Code.JmpIfAOne -> if (a == 1) copy(ptr = ptr + code.offset) else advance()
        is Code.IncA -> copy(a = a + 1).advance()
        is Code.TripleA -> copy(a = a * 3).advance()
        is Code.HalfA -> copy(a = a shr 1).advance()
        is Code.IncB -> copy(b = b + 1).advance()
    }

    fun advance(): MachineState = copy(ptr = ptr + 1)
}

fun main() {
    val program = inputLines("/day23.txt").map(Code::interpret).toList()

    val part1 = runProgram(0, program).b
    val part2 = runProgram(1, program).b

    println("part1: $part1")
    println("part2: $part2")
}

private fun runProgram(initialA: Int, program: List<Code>): MachineState {
    var state = MachineState(0, initialA, 0)
    while (state.ptr in program.indices) {
        val code = program[state.ptr]
        state = state.runCode(code)
    }
    return state
}