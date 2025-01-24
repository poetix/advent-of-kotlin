package com.codepoetics.aoc2017

import com.codepoetics.aoc.inputLines
import com.codepoetics.mariko.api.FromPattern
import com.codepoetics.mariko.kotlin.interpret
import java.util.Deque

sealed interface DuetOperand {
    @FromPattern("[a-z]")
    data class Register(val name: Char) : DuetOperand

    @FromPattern("(-?\\d+)")
    data class Const(val value: Long) : DuetOperand
}

sealed interface DuetOpcode {
    //snd X plays a sound with a frequency equal to the value of X.
    @FromPattern("snd ([a-z])")
    data class Snd(val register: Char) : DuetOpcode

    //set X Y sets register X to the value of Y.
    @FromPattern("set ([a-z]) (.*)")
    data class Set(val register: Char, val operand: DuetOperand) : DuetOpcode

    //add X Y increases register X by the value of Y.
    @FromPattern("add ([a-z]) (.*)")
    data class Add(val register: Char, val operand: DuetOperand) : DuetOpcode

    //mul X Y sets register X to the result of multiplying the value contained in register X by the value of Y.
    @FromPattern("mul ([a-z]) (.*)")
    data class Mul(val register: Char, val operand: DuetOperand) : DuetOpcode

    //mod X Y sets register X to the remainder of dividing the value contained in register X by the value of Y (that is, it sets X to the result of X modulo Y).
    @FromPattern("mod ([a-z]) (.*)")
    data class Mod(val register: Char, val operand: DuetOperand) : DuetOpcode

    //rcv X recovers the frequency of the last sound played, but only when the value of X is not zero. (If it is zero, the command does nothing.)
    @FromPattern("rcv ([a-z])")
    data class Rcv(val register: Char) : DuetOpcode

    //jgz X Y jumps with an offset of the value of Y, but only if the value of X is greater than zero. (An offset of 2 skips the next instruction, an offset of -1 jumps to the previous instruction, and so on.)
    @FromPattern("jgz (.*) (.*)")
    data class Jgz(val subject: DuetOperand, val offset: DuetOperand) : DuetOpcode
}

class DuetMachineState(private val program: List<DuetOpcode>, private val part1Behaviour: Boolean) {
    var isSuspended = false
    val frequenciesPlayed = ArrayDeque<Long>()
    var frequenciesSent = 0

    private var ptr: Int = 0
    private val registerValues: MutableMap<Char, Long> = mutableMapOf()

    override fun toString(): String {
        return "registerValues=$registerValues frequenciesPlayed=$frequenciesPlayed"
    }

    private fun getRegister(register: Char): Long = registerValues[register] ?: 0

    fun setRegister(register: Char, value: Long) {
        registerValues[register] = value
    }

    private fun updateRegister(register: Char, update: (Long) -> Long): Unit {
        registerValues.compute(register) { _, existing -> update(existing ?: 0L) }
    }

    private fun operandValue(operand: DuetOperand): Long = when(operand) {
        is DuetOperand.Register -> getRegister(operand.name)
        is DuetOperand.Const -> operand.value
    }

    fun runToSuspension() {
        while (!isSuspended && ptr in program.indices) {
            executeNext()
        }
    }

    fun resume(frequency: Long) {
        val opcode = program[ptr] as DuetOpcode.Rcv
        setRegister(opcode.register, frequency)
        isSuspended = false
        ptr++
        runToSuspension()
    }

    private fun executeNext() {
        var jmp = 0
        val opcode = program[ptr]
        when(opcode) {
            is DuetOpcode.Set -> setRegister(opcode.register, operandValue(opcode.operand))
            is DuetOpcode.Add -> updateRegister(opcode.register) { it + operandValue(opcode.operand) }
            is DuetOpcode.Mul -> updateRegister(opcode.register) { it * operandValue(opcode.operand) }
            is DuetOpcode.Mod -> updateRegister(opcode.register) { it % operandValue(opcode.operand) }
            is DuetOpcode.Snd -> {
                frequenciesPlayed.add(getRegister(opcode.register))
                frequenciesSent++
            }
            is DuetOpcode.Rcv -> if (!part1Behaviour || getRegister(opcode.register) != 0L) isSuspended = true
            is DuetOpcode.Jgz -> jmp = if (operandValue(opcode.subject) > 0) operandValue(opcode.offset).toInt() else 0
        }
        if (isSuspended) return
        ptr += if (jmp != 0) jmp else 1
    }
}

fun main() {
    val program = inputLines("/2017/day18.txt").interpret<DuetOpcode>().toList()
    val state = DuetMachineState(program, true)
    state.runToSuspension()

    val part1 = state.frequenciesPlayed.last()
    println("part1: $part1")

    val machine1 = DuetMachineState(program, false).apply { setRegister('p', 0) }
    val machine2 = DuetMachineState(program, false).apply { setRegister('p', 1) }

    machine1.runToSuspension()
    machine2.runToSuspension()

    while ((machine1.isSuspended && machine2.frequenciesPlayed.isNotEmpty()) ||
        (machine2.isSuspended && machine1.frequenciesPlayed.isNotEmpty())) {
        while (machine1.frequenciesPlayed.isNotEmpty() && machine2.isSuspended) {
            machine2.resume(machine1.frequenciesPlayed.removeFirst())
        }
        while (machine2.frequenciesPlayed.isNotEmpty() && machine1.isSuspended) {
            machine1.resume(machine2.frequenciesPlayed.removeFirst())
        }
    }

    val part2 = machine2.frequenciesSent
    println("part2: $part2")
}