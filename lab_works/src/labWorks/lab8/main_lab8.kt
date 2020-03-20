package lab8

import lab8.data.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.lang.Boolean
import javax.xml.XMLConstants
import javax.xml.bind.JAXBContext
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.validation.SchemaFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

private const val XML_DATA_PATH = "/Users/leonid/Univer/4_2/Parallel/labs/lab_works_89/src/lab8/tariffs_data.xml"
private const val XML_DATA_ITEM_PATH = "/Users/leonid/Univer/4_2/Parallel/labs/lab_works_89/src/lab8/tariffs_data_item.xml"
private const val XSD_SCHEMA_PATH = "/Users/leonid/Univer/4_2/Parallel/labs/lab_works_89/src/lab8/tariff_scheme.xsd"

fun main(args: Array<String>) {

    val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(File(XML_DATA_PATH))
    val documentItem = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(File(XML_DATA_ITEM_PATH))
    val schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(File(XSD_SCHEMA_PATH))

    parseDom(document)
    xPathParser(document)
    xPathParseItem(documentItem)

    try {
        schema.newValidator().validate(DOMSource(document))
        println("Xml document validation is succeeded")
    } catch (e: Exception) {
        println(e.message)
    }

    val tariffsFile = File(XML_DATA_PATH)
    val tariffs = JAXBContext.newInstance(Tariffs::class.java).createUnmarshaller().unmarshal(tariffsFile) as Tariffs
    tariffs.tariffElement.sortWith(Comparator { tariff1, tariff2 -> tariff1.id.compareTo(tariff2.id) })
    tariffs.tariffElement.forEach { println(it) }

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

    println("\n\n$rootTariffsJsonObject")
}

private fun parseDom(document: Document) {
    val tariffList = mutableListOf<Tariff>()
    document.documentElement.normalize()

    val tariffsNodes = document.getElementsByTagName("tariffElement")
    for (tariffIndex in 0 until tariffsNodes.length) {
        if (tariffsNodes.item(tariffIndex).nodeType == Node.ELEMENT_NODE) {
            //Tariff main
            val tariffElement = tariffsNodes.item(tariffIndex) as Element

            //CallPrices
            val callPricesItemsNodes = (tariffElement.getElementsByTagName("callPrices").item(0) as Element).getElementsByTagName("item")
            val callItems = mutableListOf<CallItem>()
            for (itemIndex in 0 until callPricesItemsNodes.length) {
                if (callPricesItemsNodes.item(itemIndex).nodeType == Node.ELEMENT_NODE) {
                    val itemElement = callPricesItemsNodes.item(itemIndex) as Element
                    val callItem = CallItem(
                            itemElement.getElementsByTagName("Type").item(0).textContent,
                            itemElement.getElementsByTagName("Price").item(0).textContent.toDouble().toInt()
                    )
                    callItems.add(callItem)
                }
            }

            //Parameters
            val parametersItemsNodes = (tariffElement.getElementsByTagName("parameters").item(0) as Element).getElementsByTagName("item")
            val parametersItems = mutableListOf<ParameterItem>()
            for (itemIndex in 0 until parametersItemsNodes.length) {
                if (parametersItemsNodes.item(itemIndex).nodeType == Node.ELEMENT_NODE) {
                    val itemElement = parametersItemsNodes.item(itemIndex) as Element
                    val parameterItem = ParameterItem(
                            itemElement.getElementsByTagName("Type").item(0).textContent,
                            itemElement.getElementsByTagName("Price").item(0).textContent.toDouble().toInt()
                    )
                    parametersItems.add(parameterItem)
                }
            }

            //Init tariff
            val tariff = Tariff(
                tariffElement.getAttribute("id"),
                tariffElement.getElementsByTagName("name").item(0).textContent,
                tariffElement.getElementsByTagName("operatorName").item(0).textContent,
                tariffElement.getElementsByTagName("payroll").item(0).textContent.toDouble().toInt(),
                tariffElement.getElementsByTagName("smsPrice").item(0).textContent.toDouble().toInt(),
                CallPrices(callItems),
                Parameters(parametersItems)
            )

            tariffList.add(tariff)
        }
    }

    println("DOM parser: $tariffList\n")
}

private fun xPathParser(document: Document) {
    val tariffList = mutableListOf<Tariff>()
    document.documentElement.normalize()

    val xPathFactory = XPathFactory.newInstance()
    val xPath = xPathFactory.newXPath()

    val expression = "/tariffs/tariffElement"
    val tariffsNodes = xPath.compile(expression).evaluate(document, XPathConstants.NODESET) as NodeList

    for (tariffIndex in 0 until tariffsNodes.length) {
        if (tariffsNodes.item(tariffIndex).nodeType == Node.ELEMENT_NODE) {
            //Tariff main
            val tariffElement = tariffsNodes.item(tariffIndex) as Element

            //CallPrices
            val callPricesItemsNodes = (tariffElement.getElementsByTagName("callPrices").item(0) as Element).getElementsByTagName("item")
            val callItems = mutableListOf<CallItem>()
            for (itemIndex in 0 until callPricesItemsNodes.length) {
                if (callPricesItemsNodes.item(itemIndex).nodeType == Node.ELEMENT_NODE) {
                    val itemElement = callPricesItemsNodes.item(itemIndex) as Element
                    val callItem = CallItem(
                            itemElement.getElementsByTagName("Type").item(0).textContent,
                            itemElement.getElementsByTagName("Price").item(0).textContent.toDouble().toInt()
                    )
                    callItems.add(callItem)
                }
            }

            //Parameters
            val parametersItemsNodes = (tariffElement.getElementsByTagName("parameters").item(0) as Element).getElementsByTagName("item")
            val parametersItems = mutableListOf<ParameterItem>()
            for (itemIndex in 0 until parametersItemsNodes.length) {
                if (parametersItemsNodes.item(itemIndex).nodeType == Node.ELEMENT_NODE) {
                    val itemElement = parametersItemsNodes.item(itemIndex) as Element
                    val parameterItem = ParameterItem(
                            itemElement.getElementsByTagName("Type").item(0).textContent,
                            itemElement.getElementsByTagName("Price").item(0).textContent.toDouble().toInt()
                    )
                    parametersItems.add(parameterItem)
                }
            }

            //Init tariff
            val tariff = Tariff(
                    tariffElement.getAttribute("id"),
                    tariffElement.getElementsByTagName("name").item(0).textContent,
                    tariffElement.getElementsByTagName("operatorName").item(0).textContent,
                    tariffElement.getElementsByTagName("payroll").item(0).textContent.toDouble().toInt(),
                    tariffElement.getElementsByTagName("smsPrice").item(0).textContent.toDouble().toInt(),
                    CallPrices(callItems),
                    Parameters(parametersItems)
            )

            tariffList.add(tariff)
        }
    }

    println("XPath parser: $tariffList\n")
}

private fun xPathParseItem(document: Document) {
    val xPathFactory = XPathFactory.newInstance()
    val xPath = xPathFactory.newXPath()

    val expressions = listOf(
            xPath.compile("/tariffElement"),
            xPath.compile("/tariffElement/name"),
            xPath.compile("/tariffElement/operatorName"),
            xPath.compile("/tariffElement/payroll"),
            xPath.compile("/tariffElement/smsPrice"),
            xPath.compile("/tariffElement/callPrices"),
            xPath.compile("/tariffElement/parameters")
    )

    val id = (expressions[0].evaluate(document, XPathConstants.NODE) as Element).getAttribute("id")
    val name = expressions[1].evaluate(document, XPathConstants.STRING) as String
    val operatorName = expressions[2].evaluate(document, XPathConstants.STRING) as String
    val payroll = expressions[3].evaluate(document, XPathConstants.STRING) as String
    val smsPrice = expressions[4].evaluate(document, XPathConstants.STRING) as String
    val callPrices = expressions[5].evaluate(document, XPathConstants.NODESET) as NodeList
    val parameters = expressions[6].evaluate(document, XPathConstants.NODESET) as NodeList

    //CallPrices
    val callPricesItemsNodes = (callPrices.item(0) as Element).getElementsByTagName("item")
    val callItems = mutableListOf<CallItem>()
    for (itemIndex in 0 until callPricesItemsNodes.length) {
        if (callPricesItemsNodes.item(itemIndex).nodeType == Node.ELEMENT_NODE) {
            val itemElement = callPricesItemsNodes.item(itemIndex) as Element
            val callItem = CallItem(
                    itemElement.getElementsByTagName("Type").item(0).textContent,
                    itemElement.getElementsByTagName("Price").item(0).textContent.toDouble().toInt()
            )
            callItems.add(callItem)
        }
    }

    //Parameters
    val parametersItemsNodes = (parameters.item(0) as Element).getElementsByTagName("item")
    val parametersItems = mutableListOf<ParameterItem>()
    for (itemIndex in 0 until parametersItemsNodes.length) {
        if (parametersItemsNodes.item(itemIndex).nodeType == Node.ELEMENT_NODE) {
            val itemElement = parametersItemsNodes.item(itemIndex) as Element
            val parameterItem = ParameterItem(
                    itemElement.getElementsByTagName("Type").item(0).textContent,
                    itemElement.getElementsByTagName("Price").item(0).textContent.toDouble().toInt()
            )
            parametersItems.add(parameterItem)
        }
    }

    //Init tariff
    val tariff = Tariff(
            id,
            name,
            operatorName,
            payroll.toDouble().toInt(),
            smsPrice.toDouble().toInt(),
            CallPrices(callItems),
            Parameters(parametersItems)
    )

    println("xPath item parser: $tariff\n")
}

//var expr: XPathExpression = xPath.compile("/tariffs/tariffElement[@id=" + "'" + id + "'" + "]/name")
//xPathTariff.setName(expr.evaluate(document, XPathConstants.STRING) as kotlin.String?)