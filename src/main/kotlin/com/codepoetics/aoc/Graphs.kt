package com.codepoetics.aoc

class WeightedGraph<T : Any> {

    data class WeightedEdge<T : Any>(val source: T, val target: T, val weight: Long)

    data class DistanceMap<T : Any>(val distances: Map<T, Long>, val precursors: Map<T, MutableSet<T>>) {

        fun getPathsTo(end: T): Sequence<Lst<T>> {
            val finishedPaths: MutableList<Lst<T>> = ArrayList()

            val precursorQueue = ArrayDeque<Lst<T>>()
            precursorQueue.add(Lst.of(end))

            while (!precursorQueue.isEmpty()) {
                val next = precursorQueue.removeFirst()
                val head = next.head

                val more = precursors[head]
                if (more == null) {
                    finishedPaths.add(next)
                    continue
                }

                more.forEach { p: T -> precursorQueue.add(p cons next) }
            }

            return finishedPaths.asSequence()
        }
    }

    private val data: IndexedMap<T, WeightedEdge<T>> = IndexedMap(WeightedEdge<T>::source)

    fun add(source: T, target: T, weight: Long) {
        data.add(WeightedEdge(source, target, weight))
    }

    private inner class DistanceMapCalculationContext {
        private val distances: MutableMap<T, Long> = HashMap()
        private val precursors: MutableMap<T, MutableSet<T>> = HashMap()

        private val queue: PriorityQueueSet<T> = PriorityQueueSet()

        fun distanceMap(start: T): DistanceMap<T> {
            initialise(start)
            populateDistanceMap()
            return DistanceMap(distances, precursors)
        }

        private fun initialise(start: T) {
            data.indices().forEach { vertex ->
                val score = if (vertex.equals(start)) 0 else Long.MAX_VALUE
                queue.add(score, vertex)
                distances.put(vertex, score)
            }
        }

        private fun populateDistanceMap() {
            while (!queue.isEmpty) {
                val source: T = queue.removeFirst()

                val distU = distances[source]!!
                if (distU == Long.MAX_VALUE) return

                data.get(source)
                    .filter { weighted -> queue.contains(weighted.target) }
                    .forEach { weighted -> updateScores(source, weighted.target, distU + weighted.weight) }
            }
        }

        private fun updateScores(source: T, target: T, newWeight: Long) {
            val distV = distances[target]

            if (newWeight > distV!!) return
            distances[target] = newWeight

            if (newWeight < distV) {
                queue.reprioritise(distV, newWeight, target)
                val newPrecursors: MutableSet<T> = HashSet()
                newPrecursors.add(source)
                precursors[target] = newPrecursors
                return
            }

            precursors.computeIfAbsent(target) { _ -> HashSet() }.add(source)
        }
    }

    fun distanceMap(start: T): DistanceMap<T> {
        val context = DistanceMapCalculationContext()
        return context.distanceMap(start)
    }
}
