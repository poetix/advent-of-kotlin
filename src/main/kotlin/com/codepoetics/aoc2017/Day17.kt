package com.codepoetics.aoc2017

class CircularDoubleLinkedListItem(val value: Int) {

    companion object {
        val initial = CircularDoubleLinkedListItem(0).apply {
            previous = this
            next = this
        }
    }

    lateinit var previous: CircularDoubleLinkedListItem
    lateinit var next: CircularDoubleLinkedListItem

    fun insertAfter(item: Int): CircularDoubleLinkedListItem {
        val newItem = CircularDoubleLinkedListItem(item)
        newItem.previous = this
        newItem.next = this.next
        newItem.next.previous = newItem
        this.next = newItem
        return newItem
    }

    fun seek(step: Int): CircularDoubleLinkedListItem {
        return (1..step).fold(this) { c, _ -> c.next }
    }
}

fun main() {
    var cursor = CircularDoubleLinkedListItem.initial
    (1..2017).forEach { i ->
        cursor = cursor.seek(324).insertAfter(i)
    }
    val part1 = cursor.next.value
    println("part1: $part1")


    var idx = 0
    var n = 0
    (1..50_000_000).forEach { i ->
        idx = (idx + 324) % i
        if (idx == 0) n = i
        idx += 1
    }
    println("part2: $n")
}