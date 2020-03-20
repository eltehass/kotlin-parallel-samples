package labWorks.lab3

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Predicate

fun main(args: Array<String>) {
    val numbers: List<Int> = (1..10000).toList().shuffled()

    println("TASK1: Elements count")
    println("Fun1, Count = ${parallelElementsCount(numbers, Predicate { it > 5000 })}")
    println("Fun2, Count = ${parallelAtomicElementsCount(numbers, Predicate { it > 5000 })}")
    println("Fun3, Count = ${parallelJustIntElementsCount(numbers, Predicate { it > 5000 })}")
    println("\n")

    println("TASK2: Min & Max")
    val minMax = parallelAtomicElementsMinMax(numbers)
    println("Min: ${minMax.first},  Max: ${minMax.second}")
    println("Min: Val=${numbers.min()} Index=${numbers.indexOf(numbers.min())}, Max: Val=${numbers.max()} Index=${numbers.indexOf(numbers.max())}")
    println("\n")

    println("TASK3: Min & Max")
    println("XorSum = ${parallelHashesXor(numbers)}")
    println("XorSum = ${serialHashesXor(numbers)}")
    println("\n")
}

/** TASK_1:
 *  Elements count
 * */

//Parallel stream
fun <T> parallelElementsCount(elements: List<T>, predicate: Predicate<T>) = elements.parallelStream().filter(predicate).count()

//Parallel stream using atomic integer
fun <T> parallelAtomicElementsCount(elements: List<T>, predicate: Predicate<T>): Int {
    val atomicResult = AtomicInteger()

    elements.parallelStream().filter(predicate).forEach {
        do {
            val prevValue = atomicResult.get()
            val nextValue = prevValue + 1
        } while (!atomicResult.compareAndSet(prevValue, nextValue))
    }

    return atomicResult.get()
}

//Parallel stream using simple integer
fun <T> parallelJustIntElementsCount(elements: List<T>, predicate: Predicate<T>): Int {
    var result = 0
    elements.parallelStream().filter(predicate).forEach { result++ }
    return result
}

/** TASK_2:
 *  Min and Max with indices
 * */

data class IndexedElement <T> (var value: T, var index: Int = 0)

fun parallelAtomicElementsMinMax(elements: List<Int>): Pair<IndexedElement<Int>, IndexedElement<Int>> {
    val atomicMin = AtomicReference<IndexedElement<Int>>(IndexedElement(Int.MAX_VALUE))
    val atomicMax = AtomicReference<IndexedElement<Int>>(IndexedElement(Int.MIN_VALUE))

    elements.indices.toList().parallelStream().forEach { indexOfElement ->
        val element = elements[indexOfElement]

        //Min
        do {
            if (element >= atomicMin.get().value) { break }
            val prevValue = atomicMin.get()
            val nextValue = IndexedElement(element, indexOfElement)
        } while (!atomicMin.compareAndSet(prevValue, nextValue))

        //Max
        do {
            if (element <= atomicMax.get().value) { break }
            val prevValue = atomicMax.get()
            val nextValue = IndexedElement(element, indexOfElement)
        } while (!atomicMax.compareAndSet(prevValue, nextValue))
    }

    return atomicMin.get() to atomicMax.get()
}

/** TASK_3:
 *  Xor of hashes
 * */

fun <T> parallelHashesXor(elements: List<T>): Int {
    val atomicResult = AtomicInteger(0)

    elements.parallelStream().map { it?.hashCode() } .forEach {
        do {
            val prevValue = atomicResult.get()
            val nextValue = it?.xor(prevValue) ?: prevValue
        } while (!atomicResult.compareAndSet(prevValue, nextValue))
    }

    return atomicResult.get()
}

fun <T> serialHashesXor(elements: List<T>): Int {
    var result = 0
    elements.map { it?.hashCode() } .forEach { result = it?.xor(result) ?: result }

    //val result2 = elements.map { it?.hashCode() }.reduce { result, element -> result?.xor(element!!)!! }
    //println("Res 1 = $result, Res 2 = $result2")

    return result
}