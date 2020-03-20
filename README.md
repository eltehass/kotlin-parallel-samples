# kotlin-parallel-samples
Parallel tasks execution using different tools of Java/Kotlin languages and third party libraries

* Lab 1: shows finding min/max values using threads and parallel streams of Kotlin programming language
* Lab 2: shows passing data between two and more threads using Threads and ThreadGroup
* Lab 3: shows comparing finding min/max values, count of filtered objects, xor of hashes.. between parallel and serial executions
* Lab 4: shows 4 popular tasks/problems like 'Producer-consumer problem', 'Readers-writers problem', 'Dining Philosophers problem' and 'Sleeping barber problem'
* Lab 5: shows work with collections as number sequences with operations like filtering, adding, intersect and other using CompletableFuture
* Lab 6: shows MPI/MPJ sample of multiprocessing work with finding min and max elements of number sequence
* Lab 7: shows work with Sockets and ServerSockets, where server sending messages only for chosen clients from file
* Lab 8: shows work with XML and XSD, checking and validating XML using XSD scheme; parsing XML to json/POJO using DOM parser, XPath parser and JAXB parser; also showed work with JSONObject and JSONArray
* Lab 9: shows work with RabbitMQ, where: one thread 'Client' sending three xml files in byte arrays to second thread; second thread 'Validator' converting bytes to xml file and checking/validation this file and if it's okay it converts this file to json and send it to third thread; third thread 'DataHolder' converting byte array to json string, parsing it using Gson library and saves to MySql database using JDBC. All communication between these three threads is done using RabbitMQ
