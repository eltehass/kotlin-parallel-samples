package labWorks.lab4

import labWorks.lab4.ResourceBarberShop.CLIENT_MAX
import labWorks.lab4.ResourceBarberShop.barberAccessObject
import labWorks.lab4.ResourceBarberShop.clientAccessObject
import labWorks.lab4.ResourceBarberShop.currentClientNumber
import labWorks.lab4.ResourceBarberShop.serviceAccessObject

object ResourceBarberShop  {

    val CLIENT_MAX = 10
    var currentClientNumber = 0

    val clientAccessObject = Object()
    val barberAccessObject = Object()
    val serviceAccessObject = Object()

//    //OPTIONAL
//    val chairsPlaces = Array(CLIENT_MAX, init = { "Chair $it" })
}

class BSClientThread(nameThread: String): Thread(nameThread) {
    override fun run() {
        if (!isInterrupted && currentClientNumber < CLIENT_MAX) {
            currentClientNumber++
            println("Came new client $name to BarberShop")

            synchronized(clientAccessObject) {
                clientAccessObject.notify()
                println("Client $name is waiting for barber")
                synchronized(barberAccessObject) {
                    barberAccessObject.wait(500)
                    println("Client $name is waiting for finishing haircut")
                    synchronized(serviceAccessObject) {
                        serviceAccessObject.wait(500)
                        println("Client $name went away with new haircut")
                    }
                }
            }
//            clientAccessObject.notify()
//            println("Client $name is waiting for barber")
//            barberAccessObject.wait()
//            println("Client $name is waiting for finishing haircut")
//            serviceAccessObject.wait()
//            println("Client $name went away with new haircut")
        } else {
            println("Client $name went without waiting")
        }
    }
}

class BSBarberThread(nameThread: String): Thread(nameThread) {
    override fun run() {
        while (!isInterrupted) {
            if (currentClientNumber == 0) {
                println("Barber $name is waiting for new client")
                synchronized(clientAccessObject) {
                    clientAccessObject.wait(500)
                }
            }

            println("Barber $name is started do haircut")
            synchronized(barberAccessObject) {
                barberAccessObject.notify()
                currentClientNumber--
                println("Barber $name is finished do haircut")
                synchronized(serviceAccessObject) {
                    serviceAccessObject.notify()
                }
            }
//            println("Barber $name is started do haircut")
//            barberAccessObject.notify()
//            currentClientNumber--
//            println("Barber $name is finished do haircut")
//            serviceAccessObject.notify()
        }
    }
}