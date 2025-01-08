package com.codepoetics.aoc

import kotlin.math.max
import kotlin.math.min

data class Region(val topLeft: Point, val bottomRight: Point) {
    val area: Long get() = (right + 1 - left) * (bottom + 1 - top)

    val containedPoints: Sequence<Point> get() = (topLeft.x until bottomRight.x + 1).asSequence().flatMap { x ->
        (topLeft.y until bottomRight.y + 1).asSequence().map { y -> Point(x, y) }
    }

    operator fun contains(point: Point): Boolean =
        point.x in (left..right) && point.y in (top..bottom)

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
        if (intersection.left > left) result.add(
            Region(
                Point(left, intersection.top),
                Point(intersection.left - 1, intersection.bottom)
            )
        )
        if (intersection.right < right) result.add(
            Region(
                Point(intersection.right + 1, intersection.top),
                Point(right, intersection.bottom)
            )
        )
        return result
    }
}