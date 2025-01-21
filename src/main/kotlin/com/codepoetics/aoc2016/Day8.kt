package com.codepoetics.aoc2016

import com.codepoetics.aoc.Point
import com.codepoetics.aoc.inputLines
import com.codepoetics.aoc.separate

private sealed interface Instruction {

    companion object {
        fun of(line: String): Instruction = when {
            line.startsWith("rect") ->
                line.split(" ")[1]
                    .split("x").let { (x, y) -> Rect(x.toLong(), y.toLong()) }
            line.startsWith("rotate row") ->
                line.split("y=")[1]
                    .split(" by ").let { (y, amt) -> RotateRow(y.toLong(), amt.toInt()) }
            else -> line.split("x=")[1]
                .split(" by ").let { (x, amt) -> RotateCol(x.toLong(), amt.toInt()) }
        }
    }

    fun applyTo(points: MutableSet<Point>)

    data class Rect(val x: Long, val y: Long) : Instruction {
        override fun applyTo(points: MutableSet<Point>) {
            (0 until x).forEach { px ->
                (0 until y).forEach { py ->
                    points.add(Point(px, py))
                }
            }
        }
    }

    data class RotateRow(val y: Long, val amt: Int) : Instruction {
        override fun applyTo(points: MutableSet<Point>) {
            val inRow = points.separate { it.y == y }
            inRow.forEach { p ->
                points.add(Point((p.x + amt) % 50, p.y))
            }
        }
    }

    data class RotateCol(val x : Long, val amt: Int) : Instruction {
        override fun applyTo(points: MutableSet<Point>) {
            val inRow = points.separate { it.x == x }
            inRow.forEach { p ->
                points.add(Point(p.x, (p.y + amt) % 6))
            }
        }
    }
}
fun main() {
    val instructions = inputLines("/2016/day8.txt").map(Instruction::of).toList()
    val lights = mutableSetOf<Point>()
    instructions.forEach { it.applyTo(lights) }

    val part1 = lights.size

    println("part1: $part1")

    (0 until 6L).forEach { y ->
        println((0 until 50L).map { x -> if (Point(x, y) in lights) '#' else '.' }.joinToString(""))
    }

    println("part2: ZFHFSFOGPO")
}