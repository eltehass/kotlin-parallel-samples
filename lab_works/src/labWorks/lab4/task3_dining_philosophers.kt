package labWorks.lab4

import java.util.concurrent.locks.Lock

class PhilosopherThread(threadName: String, private val locks: List<Lock>, private val indexAtTheTable: Int): Thread(threadName) {

    private var currentCountOfMeal = 0

    override fun run() {
        while (!isInterrupted && currentCountOfMeal < COUNT_OF_MEAL) {
            locks[indexAtTheTable].lock()
            locks[(indexAtTheTable + 1) % locks.size].lock()

            try {
                println("Philosopher #$indexAtTheTable started eating")
                sleep(100)
                println("Philosopher #$indexAtTheTable finished eating")
            } catch (e: InterruptedException) {
                println(e.message)
            } finally {
                currentCountOfMeal++
                locks[indexAtTheTable].unlock()
                locks[(indexAtTheTable + 1) % locks.size].unlock()
                println("Philosopher #$indexAtTheTable cleared cutlery")
            }
        }

        interrupt()
    }

    companion object {
        private const val COUNT_OF_MEAL = 3
    }

}