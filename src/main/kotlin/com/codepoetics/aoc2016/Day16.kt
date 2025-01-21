package com.codepoetics.aoc2016

sealed interface BitSequence {

    fun asSequence(): Sequence<Boolean>
    fun asReverseSequence(): Sequence<Boolean>
    val length: Int
    fun checksum(limit: Int): Sequence<Boolean> = (limit to asSequence().take(limit)).checksum().second

    private fun Pair<Int, Sequence<Boolean>>.checksum(): Pair<Int, Sequence<Boolean>> {
        val halfLength = first shr 1
        val checksummed = second.chunked(2).map { (a, b) -> a == b }
        return if (halfLength % 2 == 1) halfLength to checksummed
        else (halfLength to checksummed).checksum()
    }

    data class Literal(val bits: BooleanArray) : BitSequence {
        override val length: Int = bits.size
        override fun asSequence(): Sequence<Boolean> = bits.asSequence()
        override fun asReverseSequence(): Sequence<Boolean> = bits.reversed().asSequence()
    }

    data class Composed(val lhs: BitSequence) : BitSequence {
        override val length: Int = (lhs.length * 2) + 1
        override fun asSequence(): Sequence<Boolean> = sequence {
            yieldAll(lhs.asSequence())
            yield(false)
            yieldAll(lhs.asReverseSequence().map { !it })
        }

        override fun asReverseSequence(): Sequence<Boolean> = sequence {
            yieldAll(lhs.asSequence().map { !it })
            yield(false)
            yieldAll(lhs.asReverseSequence())
        }
    }
}

fun main() {
    var filled: BitSequence = BitSequence.Literal("10001001100000001".map { it == '1' }.toList().toBooleanArray())
    while (filled.length < 272) filled = BitSequence.Composed(filled)
    val part1 = filled.checksum(272).joinToString("") { if (it) "1" else "0" }

    println("part1: $part1")

    while (filled.length < 35651584) filled = BitSequence.Composed(filled)
    val part2 = filled.checksum(35651584).joinToString("") { if (it) "1" else "0" }

    println("part2: $part2")
}