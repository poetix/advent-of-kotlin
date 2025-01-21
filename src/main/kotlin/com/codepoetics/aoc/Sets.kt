package com.codepoetics.aoc

fun <T : Any> MutableSet<T>.separate(predicate: (T) -> Boolean): MutableSet<T> {
    val iter = iterator()
    val separated = mutableSetOf<T>()
    while (iter.hasNext()) {
        val next = iter.next()
        if (predicate(next)) {
            iter.remove()
            separated.add(next)
        }
    }
    return separated
}