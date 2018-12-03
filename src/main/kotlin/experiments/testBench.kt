package experiments

import data_structures.Vector
import java.math.BigDecimal
import java.math.MathContext
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Supplier
import kotlin.math.log2

private fun randomSort(num_elements: Int, algorithm: (Vector<Int>, Comparator<Int>) -> Unit): Long {
    val list = (0 until num_elements).map { ThreadLocalRandom.current().nextInt(Int.MIN_VALUE, Int.MAX_VALUE) }.toList()
    val indices = Vector<Int>()
    list.indices.forEach { indices.insertAt(0, it) }

    var cmps = 0L

    algorithm(indices, Comparator { a, b ->
        cmps++
        Integer.compare(list[a!!], list[b!!])
    })

    var prev = list[indices.get(0)]
    for (i in 1 until list.size) {
        val cur = list[indices.get(i.toLong())]
        if (prev > cur) {
            println("Error: [${i - 1}] = $prev, [$i] = $cur")
            System.exit(0)
        }
        prev = cur
    }
    return cmps
}

fun randomSort(num_elements: Int, num_repeats: Int, algorithm: (Vector<Int>, Comparator<Int>) -> Unit): BigDecimal {

    val executor = Executors.newFixedThreadPool(8)
    val tasks = (0 until num_repeats).map { CompletableFuture.supplyAsync(Supplier { randomSort(num_elements, algorithm) }, executor) }
    val comparisons = tasks.map { it.get() }.sum().toBigDecimal().divide(num_repeats.toBigDecimal(), MathContext.DECIMAL128)
    val avg_factor = ((comparisons - num_elements.toBigDecimal() * log2(num_elements + .0).toBigDecimal())).divide(num_elements.toBigDecimal(), MathContext.DECIMAL128);
    executor.shutdown()

    return avg_factor
}

fun iteration_count(num_elements: Int): Int {
    return when {
        num_elements < 5000 -> 50000
        num_elements < 10000 -> 10000
        num_elements < 50000 -> 5000
        num_elements < 100000 -> 1000
        num_elements < 500000 -> 500
        num_elements < 1000000 -> 250
        num_elements < 2000000 -> 100
        else -> 50
    }
}

fun insertMiddleValues(l: MutableList<Int>) {
    val iterator = l.listIterator()
    var last = iterator.next()
    while (iterator.hasNext()) {
        val current = iterator.next()
        iterator.previous()
        iterator.add((last + current) / 2)
        last = iterator.next()
    }
}