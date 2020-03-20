package labWorks.lab4

data class SomeArticle(
        private var title: String = "Default title",
        private var description: String = "Default description",
        private var authorName: String = "Default author") {

    fun rewriteArticle(title: String, description: String, authorName: String) {
        this.title = title
        this.description = description
        this.authorName = authorName
    }

    fun readArticle(): String = "Title: $title, Description: $description, by author: $authorName"
}

class WriterThread(threadName: String, private val someArticle: SomeArticle, private val articleAccessObject: Object) : Thread(threadName) {

    private var articlesCountByThisAuthor = 0

    init {
        writers_count++
    }

    override fun run() {
        sleep(500)
        synchronized(articleAccessObject) {
            articlesCountByThisAuthor++
            someArticle.rewriteArticle("Title #$articlesCountByThisAuthor", "Description $articlesCountByThisAuthor", name)
            sleep(900)
            println("Article is written: ${someArticle.readArticle()}")
            articleAccessObject.notify()
        }
    }

    companion object {
        var writers_count = 0
    }
}

class ReaderThread(threadName: String, private val someArticle: SomeArticle, private val articleAccessObject: Object, private val readersCountAccessObject: Object) : Thread(threadName) {

    init {
        readers_count++
    }

    override fun run() {
        while (!isInterrupted) {
            synchronized(readersCountAccessObject) {
                activeReadersCount++
                if (activeReadersCount == 1) {
                    synchronized(articleAccessObject) {
                        readArticle()
                    }
                } else {
                    readArticle()
                }
                sleep(300)
                reading_count++
            }

            if (reading_count >= readers_count * WriterThread.writers_count) {
                interrupt()
            }
        }
    }

    private fun readArticle() {
        readersCountAccessObject.notify()
        println("Article is read by $name: ${someArticle.readArticle()}")
        synchronized(readersCountAccessObject) {
            activeReadersCount--
            if (activeReadersCount == 0) {
                articleAccessObject.notify()
            }
            readersCountAccessObject.notify()
        }
    }

    companion object {
        private var activeReadersCount = 0
        var readers_count = 0
        var reading_count = 0
    }
}