package com.codepoetics.aoc

fun <T> Collection<T>.discretePairs(): Sequence<Pair<T, T>> =
    asSequence().flatMapIndexed { i, left ->
        (i + 1 until size).map { j -> left to elementAt(j) }
    }

fun <T> Collection<Collection<T>>.transpose(): Collection<Collection<T>> =
    if (isEmpty()) this else {
        val result = first().map { mutableListOf(it) }
        asSequence().drop(1).forEach { next ->
            next.asSequence().zip(result.asSequence()).forEach { (item, list) ->
                list.add(item)
            }
        }
        result
    }

