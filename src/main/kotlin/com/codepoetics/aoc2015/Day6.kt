package com.codepoetics.aoc2015

import com.codepoetics.aoc.Point
import com.codepoetics.aoc.Region
import com.codepoetics.aoc.inputLines
import kotlin.math.max

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