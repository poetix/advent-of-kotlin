package com.codepoetics.aoc

fun <T> String.ifMatch(ctor: (MatchResult.Destructured) -> T): (String) -> T? =
    Regex(this).let { regex ->
        { input ->
            regex.find(input)?.let { ctor(it.destructured) }
        }
    }

fun <T> firstMatch(input: String, vararg matchers: (String) -> T?): T? =
    matchers.asSequence().mapNotNull { it(input) }.firstOrNull()