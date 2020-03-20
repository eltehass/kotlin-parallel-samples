package lab9

import labWorks.lab9.ValidatorThread

private const val HOST_NAME = "localhost"

fun main(args: Array<String>) {
    val clientThread = ClientThread(HOST_NAME).apply { start() }
    val validatorThread = ValidatorThread(HOST_NAME).apply { start() }
    val dataHolderThread = DataHolderThread(HOST_NAME).apply { start() }
}