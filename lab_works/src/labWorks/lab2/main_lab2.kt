package labWorks.lab2

import java.util.*

/**
 * 3. Программа моделирует обслуживание одного потока процессов несколькими
 * центральными процессорами компьютера без очередей. Если процесс сгенерирован в тот
 * момент, когда первый процессор занят, процесс берется на обработку вторым
 * процессором, если и этот процессор занят, запускается третий процессор и т.д.
 *
 * Определить количество задействованных процессоров и процент от общего количества
 * процессов, которые были обработаны каждым процессором.
 * **/

class ProcessTask(val intervalTimeBetweenTasksMillis: Long = -1L)

class ThreadCreator(groupName: ThreadGroup, val tasksCount: Int, val executors: List<ThreadExecutor>, val tasks: Queue<ProcessTask> = ArrayDeque()) : Thread(groupName, "ThreadCreator") {
    var createdTasksCount = 0

    override fun run() {
        while (createdTasksCount < tasksCount) {
            tasks.add(ProcessTask((Math.random() * 1000).toLong()))
            createdTasksCount++
            println("$name: Created Task with interval time = ${tasks.peek().intervalTimeBetweenTasksMillis}")

            val sleepInterval = if (executors.count { it.processTask == null } > 0 && tasks.size > 0) {
                val currentExecutor = executors.first { it.processTask == null }
                val currentTask = tasks.poll()

                currentExecutor.processTask = currentTask
                println("$name: Scheduled Task with interval time = ${currentTask.intervalTimeBetweenTasksMillis}")

                currentTask.intervalTimeBetweenTasksMillis
            } else if (tasks.size > 0) {
                tasks.peek().intervalTimeBetweenTasksMillis
            } else {
                tasks.add(ProcessTask((Math.random() * 1000).toLong()))
                tasks.peek().intervalTimeBetweenTasksMillis
            }

            sleep(sleepInterval)
        }

        while (tasks.isNotEmpty()) {
            if (executors.count { it.processTask == null } > 0) {
                val currentExecutor = executors.first { it.processTask == null }
                currentExecutor.processTask = tasks.poll()
            }
        }
    }
}

/*
class ThreadExecutorWrapper(val name: String, val groupName: ThreadGroup,
                            val executingTimeMillis: Long) {

    private var worker: Thread? = null
    var finishedTasks = 0

    fun startWork(processTask: ProcessTask): ProcessTask? {
        if (isWorking()) {
            return processTask
        }

        worker = Thread(groupName,  Runnable {
            Thread.sleep(executingTimeMillis)
            finishedTasks++
            println("$name: Finished Task with interval time = ${processTask.intervalTimeBetweenTasksMillis}")
            println("$name: Finished Tasks count = $finishedTasks")
            worker = null
        }, name)

        return null
    }

    private fun finishWork() {
        worker?.interrupt()
        worker = null
    }

    fun isWorking(): Boolean = worker?.isAlive ?: false

}
*/

class ThreadExecutor(name: String, groupName: ThreadGroup,
                     val executingTimeMillis: Long,
                     var processTask: ProcessTask? = null) : Thread(groupName, name) {
    var finishedTasks = 0

    override fun run() {
        while (!interrupted()) {
            if (processTask != null) {
                sleep(executingTimeMillis)
                finishedTasks++
                println("$name: Finished Task with interval time = ${processTask?.intervalTimeBetweenTasksMillis}")
                println("$name: Finished Tasks count = $finishedTasks")
                processTask = null
            } else {
                sleep(1)
            }
        }
    }
}

fun main(args: Array<String>) {
    val taskCount = 10
    val executorsCount = 7
    val threadGroup = ThreadGroup(Thread.currentThread().threadGroup, "LeoThreadsGroup")

    val executors = mutableListOf<ThreadExecutor>()
    repeat(executorsCount) {
        val thread = ThreadExecutor("ThreadExecutor$it", threadGroup, (Math.random() * 7000).toLong())
        thread.isDaemon = true
        thread.start()
        executors.add(thread)
    }

    val threadCreator = ThreadCreator(threadGroup, taskCount, executors)
    threadCreator.start()
}