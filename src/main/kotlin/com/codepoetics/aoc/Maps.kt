package com.codepoetics.aoc

fun <K> MutableMap<K, Int>.increment(key: K) =
    compute(key) { _, c -> (c ?: 0) + 1}

fun <K> MutableMap<K, Int>.decrement(key: K) =
    compute(key) { _, c -> (c!! - 1).let { if (it == 0) null else it} }


class IndexedMap<K : Any, V : Any>(private val index: (V) -> K) {

    private val inner: MutableMap<K, MutableSet<V>> = mutableMapOf()

    fun add(value: V) {
        inner.computeIfAbsent(index(value)) { _ -> HashSet() }.add(value)
    }

    fun remove(value: V): Boolean {
        val indexOfValue = index(value)
        val container = inner[indexOfValue] ?: return false

        val result = container.remove(value)
        if (container.isEmpty()) {
            inner.remove(indexOfValue)
        }
        return result
    }

    fun indices(): Sequence<K> {
        return inner.keys.asSequence()
    }

    fun get(index: K): Sequence<V> {
        val container = inner[index] ?: return emptySequence()
        return container.asSequence()
    }

    companion object {
        fun <K : Any, V : Any> of(values: Sequence<V>, index:(V) -> K): IndexedMap<K, V> {
            return IndexedMap(index).also {map ->  values.forEach { map.add(it) } }
        }
    }
}