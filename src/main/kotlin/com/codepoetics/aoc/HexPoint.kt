package com.codepoetics.aoc

import kotlin.math.abs
import kotlin.math.max

enum class HexDirection(val transform: HexPoint.() -> HexPoint) {
    N({ HexPoint(a, r - 1, c) }),
    S({ HexPoint(a, r + 1, c) }),
    NE({ HexPoint(!a, r - if (a) 1 else 0, c + if (a) 1 else 0)}),
    SE({ HexPoint(!a, r + if (a) 0 else 1, c + if (a) 1 else 0)}),
    NW({ HexPoint(!a, r - if (a) 1 else 0, c - if (!a) 1 else 0)}),
    SW({ HexPoint(!a, r + if (a) 0 else 1, c - if (!a) 1 else 0)})
}
data class HexPoint(val a: Boolean, val r: Long, val c: Long) {

    companion object {
        val ORIGIN = HexPoint(false, 0, 0)
    }

    val n: HexPoint get() = HexDirection.N.transform(this)
    val s: HexPoint get() = HexDirection.S.transform(this)
    val nw: HexPoint get() = HexDirection.NW.transform(this)
    val ne: HexPoint get() = HexDirection.NE.transform(this)
    val sw: HexPoint get() = HexDirection.SW.transform(this)
    val se: HexPoint get() = HexDirection.SE.transform(this)

    val size: Long get() {
        val diagonalDistance = abs(c) * 2
        val verticalDistance = max(0, abs(r) - (if (a) 1 else 0) - abs(c))
        return diagonalDistance + verticalDistance + (if (a) 1 else 0)
    }

    operator fun plus(other: HexPoint): HexPoint =
        HexPoint(
            a xor other.a,
            r + other.r + (if (a && other.a) 1 else 0),
            c + other.c + (if (a && other.a) 1 else 0))

    operator fun unaryMinus(): HexPoint =
        HexPoint(
            a,
            -r - (if (a) 1 else 0),
            -c - (if (a) 1 else 0))

    operator fun minus(other: HexPoint) = plus(other.unaryMinus())
}