package com.codepoetics.aoc

import java.util.*

class PriorityQueueSet<T> {
    private val byPriority: SortedMap<Long, MutableSet<T>> = TreeMap()
    private val unprioritised: MutableSet<T> = HashSet()

    fun add(priority: Long, value: T) {
        byPriority.computeIfAbsent(priority) { _ -> HashSet() }.add(value)
        unprioritised.add(value)
    }

    fun contains(value: T): Boolean {
        return unprioritised.contains(value)
    }

    fun removeFirst(): T {
        val entryAtPriority = byPriority.firstEntry()
        val priority = entryAtPriority.key
        val atPriority = entryAtPriority.value

        val next = atPriority.iterator().next()
        atPriority.remove(next)
        unprioritised.remove(next)

        if (atPriority.isEmpty()) byPriority.remove(priority)
        return next
    }

    fun reprioritise(oldPriority: Long, newPriority: Long, value: T) {
        val atOld = byPriority[oldPriority]!!
        atOld.remove(value)
        if (atOld.isEmpty())  byPriority.remove(oldPriority)

        byPriority.computeIfAbsent(newPriority) { _ -> HashSet() }.add(value)
    }

    val isEmpty: Boolean get() = unprioritised.isEmpty()
}