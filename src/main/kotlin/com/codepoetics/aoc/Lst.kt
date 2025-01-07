package com.codepoetics.aoc

infix fun <T : Any> T.cons(other: Lst<T>): Lst<T> = when (other) {
    is Lst.Empty -> Lst.Cons(this, other as Lst<T>, this, 1)
    else -> Lst.Cons(this, other, other.last, other.length + 1)
}

fun <T : Any> Sequence<T>.toLst() = Lst.of(this)

sealed interface Lst<T : Any> : Iterable<T> {

    companion object {
        fun <T : Any> empty(): Lst<T> = Empty as Lst<T>

        fun <T : Any> of(vararg values: T): Lst<T> = of(values.asSequence())
        fun <T : Any> of(iterable: Iterable<T>): Lst<T> = of(iterable.asSequence())

        fun <T : Any> of(sequence: Sequence<T>): Lst<T> {
            var result = empty<T>()
            sequence.forEach {
                result = it cons result
            }
            return result
        }
    }

    val length: Int
    val first: T
    val last: T
    fun isEmpty(): Boolean
    fun isNotEmpty(): Boolean = !isEmpty()

    override fun iterator(): Iterator<T> = object : Iterator<T> {

        private var current = this@Lst

        override fun hasNext(): Boolean = current.isNotEmpty()

        override fun next(): T {
            return (current as Cons<T>).run {
                current = tail
                head
            }
        }
    }

    fun <R : Any> map(f: (T) -> R): Lst<R>
    fun filter(predicate: (T) -> Boolean): Lst<T>

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
        override val length: Int get() = 0
        override fun isEmpty(): Boolean = true
        override fun <R : Any> map(f: (Any) -> R): Lst<R> = empty()
        override fun filter(predicate: (Any) -> Boolean): Lst<Any> = empty()
        override val first: Any get() = error("Empty Lst has no first item")
        override val last: Any get() = error("Empty Lst has no last item")
        override fun toString() = "[]"
    }

    data class Cons<T : Any>(val head: T, val tail: Lst<T>, override val last: T, override val length: Int) : Lst<T> {
        override fun isEmpty(): Boolean = false
        override fun <R : Any> map(f: (T) -> R): Lst<R>  = f(head) cons tail.map(f)
        override fun filter(predicate: (T) -> Boolean): Lst<T> =
            if (predicate(head)) this else tail.filter(predicate)
        override val first: T get() = head
        override fun toString() = asSequence().joinToString(",", "[", "]")
    }
}