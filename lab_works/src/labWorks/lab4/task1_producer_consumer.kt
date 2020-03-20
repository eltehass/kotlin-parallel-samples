package labWorks.lab4

import java.util.*
import java.util.concurrent.Semaphore

data class CommonResource(var mutableValue: Int = -1)

class Producer(name: String, private val countOfTasks: Int, private val semaphore: Semaphore, private val resource: CommonResource) : Thread(name) {

    private var producedTasksCount = 0

    override fun run() {
        while (producedTasksCount < countOfTasks) {
            println("Producer $name is waiting for permission")
            semaphore.acquire()
            updateResource()
            semaphore.release()
            println("Producer $name released permission")
            producedTasksCount++
        }
    }

    private fun updateResource() {
        sleep(300)
        resource.mutableValue = Random().nextInt()
        println("Producer $name updated common resource with value = ${resource.mutableValue}")
    }
}

class Consumer(name: String, private val semaphore: Semaphore, private val resource: CommonResource) : Thread(name) {

    override fun run() {
        while (!isInterrupted) {
            println("Consumer $name is waiting for permission")
            semaphore.acquire()
            consumeResource()

            semaphore.release()
            println("Consumer $name released permission")
        }
    }

    private fun consumeResource() {
        if (resource.mutableValue == -1) {
            return
        }

        sleep(300)
        println("Consuming resource with new value = ${resource.mutableValue}")
        resource.mutableValue = -1
    }
}