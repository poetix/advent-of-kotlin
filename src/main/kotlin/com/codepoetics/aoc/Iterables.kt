package com.codepoetics.aoc

fun <T> Collection<T>.discretePairs(): Sequence<Pair<T, T>> =
    asSequence().flatMapIndexed { i, left ->
        (i + 1 until size).map { j -> left to elementAt(j) }
    }