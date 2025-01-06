package com.codepoetics.aoc

import kotlin.streams.asSequence

fun inputLines(name: String): Sequence<String> =
    object {}.javaClass.getResourceAsStream(name)?.bufferedReader()?.lines()?.asSequence()
        ?: throw Exception("$name not found")