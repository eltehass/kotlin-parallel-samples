package lab9

import com.rabbitmq.client.ConnectionFactory
import java.io.File

class ClientThread(private val hostName: String = "localhost") : Thread() {

    override fun run() {
        val xmlDataPaths = listOf(XML_DATA_PATH1, XML_DATA_PATH2, XML_DATA_PATH3)
        xmlDataPaths.forEach { XML_DATA_PATH ->
            val tariffsFile = File(XML_DATA_PATH)
            val connectionFactory = ConnectionFactory().apply { host = hostName }
            val channel = connectionFactory.newConnection().createChannel().apply {
                queueDeclare(CLIENT_QUEUE_NAME, false, false, false, null)
                basicPublish("", CLIENT_QUEUE_NAME, null, tariffsFile.readBytes())
                println("ClientThread: send file bytes, $XML_DATA_PATH")
            }
        }
    }

    companion object {
        private const val XML_DATA_PATH1 = "/Users/leonid/Univer/4_2/Parallel/labs/lab_works_89/src/lab9/data/tariffs_data1.xml"
        private const val XML_DATA_PATH2 = "/Users/leonid/Univer/4_2/Parallel/labs/lab_works_89/src/lab9/data/tariffs_data2.xml"
        private const val XML_DATA_PATH3 = "/Users/leonid/Univer/4_2/Parallel/labs/lab_works_89/src/lab9/data/tariffs_data3.xml"
        const val CLIENT_QUEUE_NAME = "client_queue"
    }

}