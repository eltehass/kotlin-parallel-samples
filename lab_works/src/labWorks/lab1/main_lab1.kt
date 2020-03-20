package labWorks.lab1

import java.lang.Exception
import java.util.*
import kotlin.system.measureTimeMillis

/**
 *   3. Створити вектор з N >= 1000 елементами з випадкових чисел. Знайти максимальний та
 *   мінімальний елементи векторів.
 *   **/

fun main(args: Array<String>) {
    val numbers = getRandomList(10000000, Random())
    println(numbers.parallelStream().min { o1, o2 -> o1.compareTo(o2) })

    //Serial
    val serialTime = measureTimeMillis {
        val result = serialSearchMinElement(numbers)
        println("Serial Result = $result")
    }

    println("Serial Time = $serialTime \n\n")

    //Parallel
    for (executorsCount in 2 .. 7) {
        val time = measureTimeMillis {
            val result = parallelSearchingMinElement(numbers, 2)
            println("Parallel Result = $result")
        }

        val acceleration = serialTime / time.toFloat()
        val efficiency = acceleration / executorsCount

        println("Executors count = $executorsCount")
        println("ParallelTime Time = $time")
        println("Acceleration = $acceleration")
        println("Efficiency = $efficiency\n\n")
    }
}

class MinSearcherThread(val numbers: List<Int>, val indexFirst: Int, val indexLast: Int, var minElement: Int = Int.MAX_VALUE) : Thread() {
    override fun run() {
        minElement = numbers.subList(indexFirst, indexLast).min() ?: minElement
    }
}

fun getRandomList(size: Int, random: Random): List<Int> = mutableListOf<Int>().apply {
    for (i in 0 until size) {
        add(random.nextInt())
    }
}

fun serialSearchMinElement(numbers: List<Int>): Int = numbers.min() ?: -1

fun parallelSearchingMinElement(numbers: List<Int>, threadsCount: Int): Int {
    val threads = mutableListOf<MinSearcherThread>()
    for (index in 0 until threadsCount) {
        val firstIndex = numbers.size / threadsCount * index
        val lastIndex = if (index == threadsCount - 1) numbers.size else numbers.size / threadsCount * (index + 1)
        threads.add(MinSearcherThread(numbers, firstIndex, lastIndex))
        threads[index].start()
    }

    threads.forEach { it.join() }
    return threads.minBy { it.minElement }?.minElement ?: -1
}