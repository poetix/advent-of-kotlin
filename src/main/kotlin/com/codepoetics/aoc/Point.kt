package com.codepoetics.aoc

import kotlin.math.abs

val ORIGIN = Point(0, 0)
val EAST = Point(1, 0)
val WEST = Point(-1, 0)
val NORTH = Point(0, -1)
val SOUTH = Point(0, 1)
val NORTH_EAST = NORTH + EAST
val NORTH_WEST = NORTH + WEST
val SOUTH_EAST = SOUTH + EAST
val SOUTH_WEST = SOUTH + WEST

fun nsewFrom(c: Int) = nsewFrom(c.toChar())

fun nsewFrom(c: Char): Point = when(c) {
    '>' -> EAST
    '<' -> WEST
    '^' -> NORTH
    'v' -> SOUTH
    else -> error("Unrecognised character $c")
}

fun udlrFrom(c: Char): Point = when(c) {
    'R' -> EAST
    'L' -> WEST
    'U' -> NORTH
    'D' -> SOUTH
    else -> error("Unrecognised character $c")
}

data class Point(val x: Long, val y: Long) : Comparable<Point> {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)

    override fun compareTo(other: Point): Int =
        x.compareTo(other.x).let { if (it == 0) y.compareTo(other.y) else it }

    val nsew: Sequence<Point> get() = sequenceOf(
        this + NORTH,
        this + EAST,
        this + SOUTH,
        this + WEST)

    val adjacent: Sequence<Point> get() = sequenceOf(
        this + NORTH_WEST,
        this + NORTH,
        this + NORTH_EAST,
        this + EAST,
        this + SOUTH_EAST,
        this + SOUTH,
        this + SOUTH_WEST,
        this + WEST
    )

    fun rotate90Left(): Point = Point(-y, x)
    fun rotate90Right(): Point = Point(y, -x)
    operator fun times(times: Int): Point = Point(x * times, y * times)

    fun manhattanDistanceFrom(other: Point) = abs(x - other.x) + abs(y - other.y)
}