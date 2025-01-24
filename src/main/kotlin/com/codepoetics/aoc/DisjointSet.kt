package com.codepoetics.aoc

import java.util.*

class DisjointSet<T : Any> {
    private val parents: MutableMap<T, T> = mutableMapOf()
    private val ranks: MutableMap<T, Int> = mutableMapOf()

    fun add(element: T) {
        parents.putIfAbsent(element, element)
        ranks.putIfAbsent(element, 0)
    }

    @SafeVarargs
    fun addAll(vararg elements: T) = Arrays.stream(elements).forEach(this::add)

    fun connect(first: T, second: T) {
        val root1 = findRoot(first)
        val root2 = findRoot(second)

        if (root1 == root2) return

        when (ranks[root1]!!.compareTo(ranks[root2]!!)) {
            LESS_THAN -> parents[root1] = root2
            GREATER_THAN -> parents[root2] = root1
            else -> {
                parents[root2] = root1
                ranks[root1] = ranks[root1]!! + 1
            }
        }
    }

    fun isConnected(first: T, second: T): Boolean {
        return findRoot(first) == findRoot(second)
    }

    fun contains(element: T): Boolean {
        return parents.containsKey(element)
    }

    private fun findRoot(element: T): T =
        parents.compute(element) { _, parent ->
            if (parent!! != element) findRoot(parent) else parent
        }!!

    fun findGroups(): Sequence<Set<T>> {
        return parents.asSequence()
            .groupingBy { (_, v) -> findRoot(v) }
            .fold(mutableSetOf<T>()) { s, (k, _) -> s.apply { add(k) } }
            .values.asSequence()
    }

    fun groupContaining(element: T): Sequence<T> {
        val root = findRoot(element)
        return parents.asSequence()
            .filter { (_, v) -> findRoot(v) == root }
            .map { (_, k) -> k }
    }

    fun elementCount(): Int {
        return parents.size
    }

    fun groupCount(): Int = parents.keys.asSequence().map(this::findRoot).distinct().count()

    companion object {
        private const val LESS_THAN = -1
        private const val GREATER_THAN = 1
    }
}