package labWorks.lab5

import java.util.*
import java.util.concurrent.CompletableFuture

fun main(args: Array<String>) {

//    listOf(1,2,3,4).parallelStream().reduce { t: Int, u: Int -> t + u }.apply { println(this.get()) }

    task10()
}

fun task1(arraySize: Int = 5) {
    val sortedAndFilteredList1 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list1: $list")
        val avg = list.average()
        println("Avg of list1: $avg")
        list.removeAll { it <= avg }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list1: $list")
        list.toList()
    }

    val sortedAndFilteredList2 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list2: $list")
        val avg = list.average()
        println("Avg of list2: $avg")
        list.removeAll { it >= avg }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list2: $list")
        list.toList()
    }

    sortedAndFilteredList1.thenCombine(sortedAndFilteredList2) { list1, list2 -> list1.plus(list2).sorted() }.thenAccept { println("Result:\n$it") }.get()
}

fun task2(arraySize: Int = 5) {
    val sortedAndFilteredList1 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list1: $list")
        val max = list.max()!! * 0.7
        println("Max * 0,7 of list1: $max")
        list.removeAll { it <= max }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list1: $list")
        list.toList()
    }

    val sortedAndFilteredList2 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list2: $list")
        list.removeAll { it % 3 != 0 }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list2: $list")
        list.toList()
    }

    sortedAndFilteredList1.thenCombine(sortedAndFilteredList2) { list1, list2 -> list1.plus(list2).sorted() }.thenAccept { println("Result:\n$it") }.get()
}

fun task3(arraySize: Int = 5) {
    val sortedAndFilteredList1 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list1: $list")
        list.map { it * 3 }.toMutableList()
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list1: $list")
        list.toList()
    }

    val sortedAndFilteredList2 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list2: $list")
        list.removeAll { it % 2 != 0 }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list2: $list")
        list.toList()
    }

    val sortedAndFilteredList3 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list3: $list")
        val avg = list.average()
        println("Avg of list3: $avg")
        list.removeAll { !((avg * 0.8).toInt() .. (avg * 1.2).toInt()).contains(it) }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list3: $list")
        list.toList()
    }

    sortedAndFilteredList1
            .thenCombine(sortedAndFilteredList2) { list1, list2 -> list1.plus(list2).sorted() }
            .thenCombine(sortedAndFilteredList3) { lists12, list3 -> lists12.minus(list3).sorted()  }
            .thenAccept { println("Result:\n$it") }.get()
}

fun task4(arraySize: Int = 5) {
    val sortedAndFilteredList1 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list1: $list")
        val max = list.max()!! * 0.6
        println("Max * 0,6 of list1: $max")
        list.removeAll { it <= max }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list1: $list")
        list.toList()
    }

    val sortedAndFilteredList2 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list2: $list")
        list.removeAll { it % 2 != 0 }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list2: $list")
        list.toList()
    }

    sortedAndFilteredList1.thenCombine(sortedAndFilteredList2) { list1, list2 -> list1.intersect(list2).sorted() }.thenAccept { println("Result:\n$it") }.get()
}

fun task5(arraySize: Int = 5) {
    val sortedAndFilteredList1 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list1: $list")
        val avg = list.average()
        println("Avg of list1: $avg")
        list.removeAll { it <= avg }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list1: $list")
        list.toList()
    }

    val sortedAndFilteredList2 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list2: $list")
        val avg = list.average()
        println("Avg of list2: $avg")
        list.removeAll { it >= avg }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list2: $list")
        list.toList()
    }

    sortedAndFilteredList1.thenCombine(sortedAndFilteredList2) { list1, list2 -> list1.minus(list2).sorted() }.thenAccept { println("Result:\n$it") }.get()
}

fun task6(arraySize: Int = 5) {
    val sortedAndFilteredList1 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list1: $list")
        list.map { it * 5 }.toMutableList()
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list1: $list")
        list.toList()
    }

    val sortedAndFilteredList2 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list2: $list")
        list.removeAll { it % 2 != 0 }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list2: $list")
        list.toList()
    }

    val sortedAndFilteredList3 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list3: $list")
        val max = list.max()!!
        println("Max of list3: $max")
        list.removeAll { !((max * 0.4).toInt() .. (max * 0.6).toInt()).contains(it) }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list3: $list")
        list.toList()
    }

    sortedAndFilteredList1
            .thenCombine(sortedAndFilteredList2) { list1, list2 -> list1.plus(list2) }
            .thenCombine(sortedAndFilteredList3) { lists12, list3 -> lists12.plus(list3).sorted()  }
            .thenAccept { println("Result:\n$it") }.get()
}

fun task7(arraySize: Int = 5) {
    val sortedAndFilteredList1 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list1: $list")
        val max = list.max()!! * 0.7
        println("Max * 0,7 of list1: $max")
        list.removeAll { it <= max }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list1: $list")
        list.toList()
    }

    val sortedAndFilteredList2 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list2: $list")
        list.removeAll { it % 3 != 0 }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list2: $list")
        list.toList()
    }

    sortedAndFilteredList1.thenCombine(sortedAndFilteredList2) { list1, list2 -> list2.minus(list1).sorted() }.thenAccept { println("Result:\n$it") }.get()
}

fun task8(arraySize: Int = 5) {
    val sortedAndFilteredList1 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list1: $list")
        val avg = list.average()
        println("Avg of list1: $avg")
        list.removeAll { it <= avg }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list1: $list")
        list.toList()
    }

    val sortedAndFilteredList2 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list2: $list")
        val avg = list.average()
        println("Avg of list2: $avg")
        list.removeAll { it >= avg }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list2: $list")
        list.toList()
    }

    sortedAndFilteredList1.thenCombine(sortedAndFilteredList2) { list1, list2 -> list1.plus(list2).sorted() }.thenAccept { println("Result:\n$it") }.get()
}

fun task9(arraySize: Int = 5) {
    val sortedAndFilteredList1 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list1: $list")
        list.map { it * 2 }.toMutableList()
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list1: $list")
        list.toList()
    }

    val sortedAndFilteredList2 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list2: $list")
        list.removeAll { it % 2 != 0 }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list2: $list")
        list.toList()
    }

    val sortedAndFilteredList3 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list3: $list")
        val max = list.max()!!
        println("Max of list3: $max")
        list.removeAll { !((max * 0.4).toInt() .. (max * 0.6).toInt()).contains(it) }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list3: $list")
        list.toList()
    }

    sortedAndFilteredList2
            .thenCombine(sortedAndFilteredList3) { list2, list3 -> list2.plus(list3).sorted() }
            .thenCombine(sortedAndFilteredList1) { lists23, list1 -> lists23.minus(list1).sorted()  }
            .thenAccept { println("Result:\n$it") }.get()
}

fun task10(arraySize: Int = 5) {
    val sortedAndFilteredList1 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list1: $list")
        val max = list.max()!! * 0.5
        println("Max * 0,5 of list1: $max")
        list.removeAll { it <= max }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list1: $list")
        list.toList()
    }

    val sortedAndFilteredList2 = CompletableFuture.supplyAsync {
        IntArray(arraySize) { Random().nextInt(50) }.asList().toMutableList()
    }.thenApplyAsync { list ->
        println("Initial list2: $list")
        list.removeAll { it % 2 != 0 }
        list
    }.thenApplyAsync {
        list -> list.sort()
        println("Sorted, filtered list2: $list")
        list.toList()
    }

    sortedAndFilteredList2
            .thenCombine(sortedAndFilteredList1) { list2, list1 -> list2.minus(list1).sorted() }
            .thenAccept { println("Result:\n$it") }.get()
}