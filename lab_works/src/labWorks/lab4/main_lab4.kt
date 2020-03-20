package labWorks.lab4

import java.util.concurrent.Semaphore
import java.util.concurrent.locks.ReentrantLock

fun main(args: Array<String>) {
    task4()
}

fun task1() {
    val commonResource = CommonResource()
    val commonSemaphore = Semaphore(1)

    val producer = Producer("Producer", 5, commonSemaphore, commonResource)
    val consumer = Consumer("Consumer", commonSemaphore, commonResource).also { it.isDaemon = true }

    producer.start()
    consumer.start()
}


fun task2() {
    val resourceLocker = Object()
    val readersCountLocker = Object()
    val article = SomeArticle()

    val readers = listOf(
            ReaderThread("Reader_1", article, resourceLocker, readersCountLocker),
            ReaderThread("Reader_2", article, resourceLocker, readersCountLocker),
            ReaderThread("Reader_3", article, resourceLocker, readersCountLocker),
            ReaderThread("Reader_4", article, resourceLocker, readersCountLocker),
            ReaderThread("Reader_5", article, resourceLocker, readersCountLocker)
    )

    val writers = listOf(
            WriterThread("Writer_1", article, resourceLocker),
            WriterThread("Writer_2", article, resourceLocker),
            WriterThread("Writer_3", article, resourceLocker),
            WriterThread("Writer_4", article, resourceLocker),
            WriterThread("Writer_5", article, resourceLocker)
    )

    readers.forEach { it.start() }
    writers.forEach { it.start() }
}

fun task3() {
    val forks = listOf(
            ReentrantLock(),
            ReentrantLock(),
            ReentrantLock(),
            ReentrantLock(),
            ReentrantLock()
    )

    val philosophers = listOf(
            PhilosopherThread("Philosopher_1", forks, 0),
            PhilosopherThread("Philosopher_2", forks, 1),
            PhilosopherThread("Philosopher_3", forks, 2),
            PhilosopherThread("Philosopher_4", forks, 3),
            PhilosopherThread("Philosopher_5", forks, 4)
    )

    philosophers.forEach { it.start() }
}

fun task4() {
    val barber = BSBarberThread("Denny").apply { isDaemon = true }
    barber.start()

    val bsClients = listOf(
            BSClientThread("BSClient_1"),
            BSClientThread("BSClient_2"),
            BSClientThread("BSClient_3"),
            BSClientThread("BSClient_4"),
            BSClientThread("BSClient_5"),
            BSClientThread("BSClient_6"),
            BSClientThread("BSClient_7"),
            BSClientThread("BSClient_8"),
            BSClientThread("BSClient_9"),
            BSClientThread("BSClient_10"),
            BSClientThread("BSClient_11"),
            BSClientThread("BSClient_12")
    )

    bsClients.forEach {
        it.start()
        Thread.sleep(150)
    }
}