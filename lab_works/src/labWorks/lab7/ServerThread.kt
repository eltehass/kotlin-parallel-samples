package labWorks.lab7

import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class ServerThread private constructor(private val client: Socket, private val clientsNames: List<String>) : Thread() {

    override fun run() {
        val inputStream = client.getInputStream()
        val outputStream = client.getOutputStream()

        val buffer = ByteArray(messageSize)
        val reader = inputStream.read(buffer)
        val dataFromClient = String(buffer, 0, reader)

        if (clientsNames.contains(dataFromClient)) {
            val messageForClient = "Hello, dear $dataFromClient!"
            outputStream.write(messageForClient.toByteArray())
        }

        client.close()
    }

    companion object {
        var serverHost: String = ""
        var serverPort: Int = 0
        const val messageSize = 64 * 1024

        fun initServer(hostName: String, port: Int, clientsNames: List<String>) {
            serverHost = hostName
            serverPort = port

            //Creating server
            val server = ServerSocket(serverPort, 0, InetAddress.getByName(serverHost))

            //Getting a client and reading message
            Thread(Runnable {
                while (true) {
                    ServerThread(server.accept(), clientsNames).apply {
                        isDaemon = true
                        start()
                    }
                }
            }).apply {
                isDaemon = true
                start()
            }
        }
    }
}