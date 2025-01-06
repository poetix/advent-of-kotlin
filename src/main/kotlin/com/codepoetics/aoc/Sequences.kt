package com.codepoetics.aoc

fun <K, V> Sequence<Pair<K, V>>.toListMultimap(): Map<K, List<V>> =
    groupBy(
        { it.first },
        { it.second })