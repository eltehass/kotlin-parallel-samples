package labWorks.lab9

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import lab8.data.Tariffs
import lab9.ClientThread
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import javax.xml.XMLConstants
import javax.xml.bind.JAXBContext
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.validation.SchemaFactory


@Suppress("INACCESSIBLE_TYPE")
class ValidatorThread(private val hostName: String = "localhost") : Thread() {

    override fun run() {
        val connectionFactory = ConnectionFactory().apply { host = hostName }
        val channel = connectionFactory.newConnection().createChannel().apply {
            // Receiving data from client queue
            queueDeclare(ClientThread.CLIENT_QUEUE_NAME, false, false, false, null)

            val deliverCallback = DeliverCallback { _, delivery ->
                // Receiving data and creating file
                val dataFile = File("validator_tariffs_data.xml").apply { createNewFile() }

                try {
                    val outputStream = FileOutputStream(dataFile)
                    outputStream.write(delivery.body)
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                // Validation xml file
                val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(dataFile)
                val schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(File(XSD_SCHEMA_PATH))

                try {
                    schema.newValidator().validate(DOMSource(document))
                    println("ValidatorThread: Xml document validation is succeeded")
                } catch (e: Exception) {
                    println(e.message)
                }

                // Converting to POJO
                val tariffs = JAXBContext.newInstance(Tariffs::class.java).createUnmarshaller().unmarshal(dataFile) as Tariffs
                tariffs.tariffElement.sortWith(Comparator { tariff1, tariff2 -> tariff1.id.compareTo(tariff2.id) })

                // Converting POJO to json
                val rootTariffsJsonObject = JSONArray()
                tariffs.tariffElement.forEach { tariff ->
                    val tariffJsonObject = JSONObject()
                    val callPricesJsonArray = JSONArray()
                    val parametersJsonArray = JSONArray()

                    tariff.callPrices.item.forEach { item ->
                        val itemJsonObject = JSONObject()
                        itemJsonObject.put("Type", item.Type)
                        itemJsonObject.put("Price", item.Price)
                        callPricesJsonArray.put(itemJsonObject)
                    }

                    tariff.parameters.item.forEach { item ->
                        val itemJsonObject = JSONObject()
                        itemJsonObject.put("Type", item.Type)
                        itemJsonObject.put("Price", item.Price)
                        parametersJsonArray.put(itemJsonObject)
                    }

                    tariffJsonObject.apply {
                        put("id", tariff.id)
                        put("name", tariff.name)
                        put("operatorName", tariff.operatorName)
                        put("payroll", tariff.payroll)
                        put("smsPrice", tariff.smsPrice)
                        put("callPrices", callPricesJsonArray)
                        put("parameters", parametersJsonArray)
                    }

                    rootTariffsJsonObject.put(tariffJsonObject)
                }

                // Sending json object to validator queue
                queueDeclare(VALIDATOR_QUEUE_NAME, false, false, false, null)
                println("ValidatorThread: send data in json format, $rootTariffsJsonObject")
                basicPublish("", VALIDATOR_QUEUE_NAME, null, rootTariffsJsonObject.toString().toByteArray())
            }

            basicConsume(ClientThread.CLIENT_QUEUE_NAME, true, deliverCallback, CancelCallback { println("Cancel: $it") })
        }
    }

    companion object {
        private const val XSD_SCHEMA_PATH = "/Users/leonid/Univer/4_2/Parallel/labs/lab_works_89/src/lab9/data/tariff_scheme.xsd"
        const val VALIDATOR_QUEUE_NAME = "validator_queue"
    }

}