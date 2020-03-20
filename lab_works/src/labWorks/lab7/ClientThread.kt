package labWorks.lab7

import java.net.Socket

class ClientThread(private val hostName: String, private val port: Int, private val clientName: String) : Thread() {

    override fun run() {
        Socket(hostName, port).apply {
            val outputStream = getOutputStream()
            val inputStream = getInputStream()

            // Sending name
            outputStream.write(clientName.toByteArray())

            // Receiving data
            val buffer = ByteArray(ServerThread.messageSize)
            val reader = inputStream.read(buffer)

            try {
                val dataFromClient = String(buffer, 0, reader)
                println("Data from server for $clientName: $dataFromClient")
            } catch (e: StringIndexOutOfBoundsException) {
                println("Data from server for $clientName: empty message")
            }
        }
    }
}