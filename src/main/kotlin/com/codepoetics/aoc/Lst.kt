package com.codepoetics.aoc

import kotlin.IndexOutOfBoundsException

infix fun <T : Any> T.cons(other: Lst<T>): Lst<T> = when (other) {
    is Lst.Empty -> Lst.Cons(this, other as Lst<T>, this, 1)
    else -> Lst.Cons(this, other, other.last, other.length + 1)
}

fun <T : Any> Iterable<T>.toLst() = Lst.of(this)

fun <T : Any> Sequence<T>.toLst() = Lst.of(this)

fun <T : Any> Lst<Lst<T>>.transpose(): Lst<Lst<T>> = if (this.isEmpty()) Lst.empty() else
    (this as Lst.Cons<Lst<T>>).let { (head, tail) ->
        val result = head.asSequence().map { Lst.of(it) }.toMutableList()

        tail.forEach { next ->
            next.forEachIndexed { i, b ->
                result[i] = b cons result[i]
            }
        }

        result.asSequence().toLst()
    }

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

    val head: T
    val tail: Lst<T>
    val length: Int
    val first: T get() = head
    val last: T

    fun isEmpty(): Boolean
    fun isNotEmpty(): Boolean = !isEmpty()
    fun remove(item: T) = filter { it != item }

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
            is Cons<T> -> choices().flatMap { (choice, remainder) ->
                remainder.permutations().map { choice cons it }
            }
        }
    }

    fun choices(): Sequence<Pair<T, Lst<T>>>

    fun conses(): Sequence<Cons<T>>

    val destructure: Destructure<T> get() = Destructure(this)

    class Destructure<T : Any>(private val wrapped: Lst<T>) {
        operator fun component1(): T = when(wrapped) {
            is Empty -> throw IndexOutOfBoundsException()
            is Cons<T> -> wrapped.head
        }

        operator fun component2(): T = when(wrapped) {
            is Empty -> throw IndexOutOfBoundsException()
            is Cons<T> -> wrapped.tail.destructure.component1()
        }

        operator fun component3(): T = when(wrapped) {
            is Empty -> throw IndexOutOfBoundsException()
            is Cons<T> -> wrapped.tail.destructure.component2()
        }

        operator fun component4(): T = when(wrapped) {
            is Empty -> throw IndexOutOfBoundsException()
            is Cons<T> -> wrapped.tail.destructure.component3()
        }

        operator fun component5(): T = when(wrapped) {
            is Empty -> throw IndexOutOfBoundsException()
            is Cons<T> -> wrapped.tail.destructure.component4()
        }
    }

    data object Empty : Lst<Any> {
        override val length: Int get() = 0
        override fun isEmpty(): Boolean = true
        override fun <R : Any> map(f: (Any) -> R): Lst<R> = empty()
        override fun filter(predicate: (Any) -> Boolean): Lst<Any> = empty()
        override val head: Any get() = error("Empty Lst has no head")
        override val tail: Lst<Any> get() = error("Empty Lst has no tail")
        override val last: Any get() = error("Empty Lst has no last item")
        override fun choices(): Sequence<Pair<Any, Lst<Any>>> = emptySequence()
        override fun conses(): Sequence<Cons<Any>> = emptySequence()

        override fun toString() = "[]"
    }

    data class Cons<T : Any>(override val head: T, override val tail: Lst<T>, override val last: T, override val length: Int) : Lst<T> {
        override fun isEmpty(): Boolean = false
        override fun <R : Any> map(f: (T) -> R): Lst<R> = f(head) cons tail.map(f)
        override fun filter(predicate: (T) -> Boolean): Lst<T> =
            if (predicate(head)) head cons tail.filter(predicate) else tail.filter(predicate)

        override fun choices(): Sequence<Pair<T, Lst<T>>> = sequence {
            yield(head to tail)
            for ((chosen, remainder) in tail.choices()) {
                yield(chosen to (head cons remainder))
            }
        }

        override fun conses(): Sequence<Cons<T>> = sequence {
            var cursor: Lst<T> = this@Cons
            while (cursor is Cons<T>) {
                yield(cursor)
                cursor = cursor.tail
            }
        }

        override fun toString() = asSequence().joinToString(",", "[", "]")
    }
}