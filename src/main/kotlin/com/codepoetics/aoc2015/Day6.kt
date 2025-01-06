package com.codepoetics.aoc2015

import com.codepoetics.aoc.Point
import com.codepoetics.aoc.inputLines
import kotlin.math.max
import kotlin.math.min

data class Region(val topLeft: Point, val bottomRight: Point) {
    val area: Long get() = (right + 1 - left) * (bottom + 1 - top)

    val containedPoints: Sequence<Point> get() = (topLeft.x until bottomRight.x + 1).asSequence().flatMap { x ->
        (topLeft.y until bottomRight.y + 1).asSequence().map { y -> Point(x, y) }
    }

    val top: Long get() = topLeft.y
    val left: Long get() = topLeft.x
    val bottom: Long get() = bottomRight.y
    val right: Long get() = bottomRight.x

    fun intersect(other: Region): Region? {
        val right = min(right, other.right)
        val left = max(left, other.left)
        val top = max(top, other.top)
        val bottom = min(bottom, other.bottom)

        return if (right >= left && bottom >= top) Region(Point(left, top), Point(right, bottom)) else null
    }

    fun difference(intersection: Region): List<Region> {
        val result = mutableListOf<Region>()
        if (intersection.top > top) result.add(Region(topLeft, Point(right, intersection.top - 1)))
        if (intersection.bottom < bottom) result.add(Region(Point(left, intersection.bottom + 1), bottomRight))
        if (intersection.left > left) result.add(Region(Point(left, intersection.top), Point(intersection.left - 1, intersection.bottom)))
        if (intersection.right < right) result.add(Region(Point(intersection.right + 1, intersection.top), Point(right, intersection.bottom)))
        return result
    }
}

data class BrightnessRegion(val region: Region, val brightness: Int) {
    val totalBrightness: Long get() = brightness * region.area
}

private val pattern = Regex("(turn on|turn off|toggle) (\\d++),(\\d++) through (\\d++),(\\d++)")

sealed interface Instruction {

    companion object {
        private fun adjustBrightness(
            region: Region,
            onRegions: MutableSet<BrightnessRegion>,
            delta: Int
        ) {
            val intersections = mutableListOf<Region>()
            val toAdd = mutableListOf<BrightnessRegion>()

            val iter = onRegions.iterator()
            while (iter.hasNext()) {
                val target = iter.next()
                val intersection = target.region.intersect(region) ?: continue
                iter.remove()
                intersections.add(intersection)
                target.region.difference(intersection).forEach { remaining ->
                    toAdd.add(BrightnessRegion(remaining, target.brightness))
                }
                toAdd.add(BrightnessRegion(intersection, max(0, target.brightness + delta)))
            }

            val minusExisting = mutableSetOf(region)
            intersections.forEach { existing ->
                TurnOff(existing).part1Apply(minusExisting)
            }

            toAdd.forEach(onRegions::add)
            minusExisting.forEach { newRegion -> onRegions.add(BrightnessRegion(newRegion, max(0, delta))) }
        }

        private fun turnOn(region: Region, onRegions: MutableSet<Region>) {
            val intersections = onRegions.mapNotNull { it.intersect(region) }

            val minusExisting = mutableSetOf(region)
            intersections.forEach { intersection ->
                turnOff(intersection, minusExisting)
            }

            onRegions.addAll(minusExisting)
        }

        private fun turnOff(region: Region, onRegions: MutableSet<Region>) {
            val iter = onRegions.iterator()
            val toAdd = mutableListOf<Region>()
            while (iter.hasNext()) {
                val target = iter.next()
                val intersection = target.intersect(region) ?: continue
                iter.remove()
                target.difference(intersection).forEach(toAdd::add)
            }
            onRegions.addAll(toAdd)
        }
    }

    fun part1Apply(onRegions: MutableSet<Region>)
    fun part2Apply(onRegions: MutableSet<BrightnessRegion>)

    data class TurnOn(val region: Region) : Instruction {
        override fun part1Apply(onRegions: MutableSet<Region>) {
            turnOn(region, onRegions)
        }

        override fun part2Apply(onRegions: MutableSet<BrightnessRegion>) {
            adjustBrightness(region, onRegions, 1)
        }
    }

    data class TurnOff(val region: Region): Instruction {
        override fun part1Apply(onRegions: MutableSet<Region>) {
            turnOff(region, onRegions)
        }

        override fun part2Apply(onRegions: MutableSet<BrightnessRegion>) {
            adjustBrightness(region, onRegions, -1)
        }
    }

    data class Toggle(val region: Region): Instruction {
        override fun part1Apply(onRegions: MutableSet<Region>) {
            val intersections = onRegions.mapNotNull { it.intersect(region) }

            turnOn(region, onRegions)
            intersections.forEach { intersection ->
                turnOff(intersection, onRegions)
            }
        }

        override fun part2Apply(onRegions: MutableSet<BrightnessRegion>) {
            adjustBrightness(region, onRegions, 2)
        }
    }
}

fun main() {
    val instructions = inputLines("/day6.txt").map { line ->
        val (instruction, leftStr, topStr, rightStr, bottomStr) = pattern.matchEntire(line)!!.destructured
        val region = Region(Point(leftStr.toLong(), topStr.toLong()), Point(rightStr.toLong(), bottomStr.toLong()))
        when (instruction) {
            "turn on" -> Instruction.TurnOn(region)
            "turn off" -> Instruction.TurnOff(region)
            "toggle" -> Instruction.Toggle(region)
            else -> error("Unrecognised instruction")
        }
    }.toList()

    val regions = mutableSetOf<Region>()
    instructions.forEach {it.part1Apply(regions) }
    val part1 = regions.sumOf(Region::area)

    val brightnessRegions = mutableSetOf<BrightnessRegion>()
    instructions.forEach { it.part2Apply(brightnessRegions) }
    val part2 = brightnessRegions.sumOf(BrightnessRegion::totalBrightness)

    println("part1: $part1")
    println("part2: $part2")
}