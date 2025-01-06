package com.codepoetics.aoc

infix fun <T : Any> T.cons(other: Lst<T>): Lst<T> = when (other) {
    is Lst.Empty -> Lst.Cons(this, other as Lst<T>, this)
    else -> Lst.Cons(this, other, other.last)
}

sealed interface Lst<T : Any> : Iterable<T> {

    companion object {
        fun <T : Any> empty(): Lst<T> = Empty as Lst<T>
        fun <T : Any> of(vararg values: T): Lst<T> {
            var result = empty<T>()
            values.forEach {
                result = it cons result
            }
            return result
        }

        fun <T : Any> of(iterable: Iterable<T>): Lst<T> {
            var result = empty<T>()
            iterable.forEach {
                result = it cons result
            }
            return result
        }
    }

    val first: T
    val last: T
    fun isEmpty(): Boolean = this is Empty

    override fun iterator(): Iterator<T> = object : Iterator<T> {

        private var current = this@Lst

        override fun hasNext(): Boolean = current !is Empty

        override fun next(): T {
            return (current as Cons<T>).run {
                current = tail
                head
            }
        }
    }

    fun <R : Any> map(f: (T) -> R): Lst<R> = when (this) {
        is Empty -> empty()
        is Cons<T> -> f(head) cons tail.map(f)
    }

    fun permutations(): Sequence<Lst<T>> = when (this) {
        is Empty -> emptySequence()
        is Cons<T> -> when (tail) {
            is Empty -> sequenceOf(this)
            is Cons<T> -> choices().asSequence().flatMap { (choice, remainder) ->
                remainder.permutations().map { choice cons it }
            }
        }
    }

    fun choices(): Lst<Pair<T, Lst<T>>> = when (this) {
        is Empty -> empty()
        is Cons<T> -> (head to tail) cons tail.choices().map { (chosen, remainder) ->
            chosen to (head cons remainder)
        }
    }

    data object Empty : Lst<Any> {
        override fun toString() = "[]"
        override val first: Any get() = error("Empty Lst has no first item")
        override val last: Any get() = error("Empty Lst has no last item")
    }

    data class Cons<T : Any>(val head: T, val tail: Lst<T>, override val last: T) : Lst<T> {
        override fun toString() = asSequence().joinToString(",", "[", "]")
        override val first: T get() = head
    }
}