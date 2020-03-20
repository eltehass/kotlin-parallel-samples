package lab9

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import labWorks.lab9.ValidatorThread
import java.nio.charset.Charset
import java.sql.Connection
import java.sql.DriverManager

class DataHolderThread(private val hostName: String = "localhost") : Thread() {

    override fun run() {
        val connectionFactory = ConnectionFactory().apply { host = hostName }
        val channel = connectionFactory.newConnection().createChannel().apply {
            // Receiving data from validator queue
            queueDeclare(ValidatorThread.VALIDATOR_QUEUE_NAME, false, false, false, null)
            val deliverCallback = DeliverCallback { _, delivery ->
                val message = String(delivery.body,  Charset.forName("UTF-8"))
                println("DataHolderThread: $message")

                val gson = Gson()

                val listType = object : TypeToken<ArrayList<LinkedTreeMap<String, Any>>>() {}.type
                val tariffs = gson.fromJson<ArrayList<LinkedTreeMap<String, Any>>>(message, listType)

                val url = "jdbc:mysql://localhost:3306/ELTEHASSDB"
                val username = "root"
                val password = "qazqaz111"

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver")
                } catch (e: Exception) {
                    println(e.message)
                }

                var connection: Connection? = null

                try {
                    connection = DriverManager.getConnection(url, username, password)

                    val statement = connection.createStatement()
                    tariffs.forEach {
                        val id = it["id"] as String
                        val name = it["name"] as String
                        val operatorName = it["operatorName"] as String
                        val payroll = it["payroll"] as Double
                        val smsPrice = it["smsPrice"] as Double
                        statement.executeUpdate("replace into Tariffs (id, name, operatorName, payroll, smsPrice) values('$id', '$name', '$operatorName', '${payroll.toInt()}', '${smsPrice.toInt()}')")

                        val callPrices = it["callPrices"] as ArrayList<LinkedTreeMap<String, Any>>
                        callPrices.forEach { callPrice ->
                            val price = callPrice["Price"] as Double
                            val typeName = callPrice["Type"] as String
                            statement.executeUpdate("replace into TariffItems (id_tariff, id_type, price, name) values('$id', '2', '${price.toInt()}', '$typeName')")
                        }

                        val parameters = it["parameters"] as ArrayList<LinkedTreeMap<String, Any>>
                        parameters.forEach { parameter ->
                            val typeName = parameter["Type"] as String
                            val price = parameter["Price"] as Double
                            statement.executeUpdate("replace into TariffItems (id_tariff, id_type, price, name) values('$id', '1', '${price.toInt()}', '$typeName')")
                        }
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
                finally {
                    connection?.close()
                }
            }

            basicConsume(ValidatorThread.VALIDATOR_QUEUE_NAME, true, deliverCallback, CancelCallback { println("Cancel: $it") })
        }
    }

}