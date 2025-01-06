package com.codepoetics.aoc

val EAST = Point(1, 0)
val WEST = Point(-1, 0)
val NORTH = Point(0, -1)
val SOUTH = Point(0, 1)

fun nsewFrom(c: Int) = nsewFrom(c.toChar())

fun nsewFrom(c: Char): Point = when(c) {
    '>' -> EAST
    '<' -> WEST
    '^' -> NORTH
    'v' -> SOUTH
    else -> error("Unrecognised character $c")
}

data class Point(val x: Long, val y: Long) {
    fun plus(other: Point) = Point(x + other.x, y + other.y)

    val nsew: Sequence<Point> get() = sequenceOf(plus(NORTH), plus(EAST), plus(SOUTH), plus(WEST))
}