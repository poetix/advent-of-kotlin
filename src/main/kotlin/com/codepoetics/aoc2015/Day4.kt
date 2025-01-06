package com.codepoetics.aoc2015

import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray())
    return digest.toHexString()
}

fun main() {
    val prefix = "bgvyzdsv"
    val part1 = (0L until Long.MAX_VALUE).find { "$prefix$it".md5().startsWith("00000") }!!
    val part2 = (0L until Long.MAX_VALUE).find { "$prefix$it".md5().startsWith("000000") }!!

    println("part1: $part1")
    println("part2: $part2")
}