package labWorks.lab7

import java.io.File

/**
 * 3. Сервер рассылает сообщения выбранным из списка клиентам.
 * Список хранится в файле.
 */

fun main(args: Array<String>) {
    val specialClientsNames = mutableListOf<String>()
    File("/Users/leonid/Univer/4_2/Parallel/labs/lab_works/src/labWorks/lab7/clients_list.txt").forEachLine {
        it.split(", ").forEach { clientName -> specialClientsNames.add(clientName) }
    }

    ServerThread.initServer("localhost", 3523, specialClientsNames)
    val clients = listOf(
            ClientThread("localhost", 3523, "Rick"),
            ClientThread("localhost", 3523, "Tolya"),
            ClientThread("localhost", 3523, "Micha"),
            ClientThread("localhost", 3523, "Rocky1"),
            ClientThread("localhost", 3523, "Vanya"),
            ClientThread("localhost", 3523, "Eddy")
    ).apply { forEach { it.start() } }

}