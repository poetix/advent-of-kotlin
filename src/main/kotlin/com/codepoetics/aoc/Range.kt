package com.codepoetics.aoc

import kotlin.math.max
import kotlin.math.min

fun Sequence<InclusiveRange>.unionAll(): Sequence<InclusiveRange> = sequence {
    val iter = this@unionAll.sortedWith(compareBy(InclusiveRange::startInclusive)
        .thenComparing(InclusiveRange::endInclusive))
        .iterator()

    var coalesced = iter.next()
    while(iter.hasNext()) {
        val r = iter.next()
        if (r.startInclusive > coalesced.endInclusive + 1) {
            yield(coalesced)
            coalesced = r
        } else {
            coalesced = InclusiveRange(min(coalesced.startInclusive, r.startInclusive),
                max(coalesced.endInclusive, r.endInclusive))
        }
    }

    yield(coalesced)
}

fun Sequence<InclusiveRange>.differenceAll(other: InclusiveRange) = sequence {
    var currentStart = other.startInclusive
    for (range in unionAll()) {
        if (range.startInclusive > currentStart) {
            yield(InclusiveRange(currentStart, range.startInclusive - 1))
        }
        currentStart = range.endInclusive + 1
    }
    if (currentStart < other.endInclusive) yield(InclusiveRange(currentStart, other.endInclusive))
}

data class InclusiveRange(val startInclusive : Long, val endInclusive : Long) {
    val isEmpty: Boolean get() = startInclusive > endInclusive

    operator fun contains(point: Long) = point in (startInclusive..endInclusive)

    fun intersects(other: InclusiveRange): Boolean {
        return other.startInclusive in this ||
                other.endInclusive in this ||
                startInclusive in other
    }

    fun isAdjacent(other: InclusiveRange): Boolean {
        return startInclusive == other.endInclusive + 1 ||
                endInclusive == other.startInclusive - 1
    }

    fun intersection(other: InclusiveRange): InclusiveRange =
            InclusiveRange(max(startInclusive, other.startInclusive),
                min(endInclusive, other.endInclusive))

    fun difference(other: InclusiveRange): Sequence<InclusiveRange> = sequence {
        if (intersection(other).isEmpty) {
            yield(this@InclusiveRange)
            return@sequence
        }
        if (other.startInclusive > startInclusive) yield(InclusiveRange(startInclusive, min(endInclusive, other.startInclusive - 1)))
        if (contains(other.startInclusive + 1)) yield(InclusiveRange(startInclusive, other.startInclusive))
    }
}