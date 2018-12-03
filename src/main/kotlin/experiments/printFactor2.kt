package experiments

import algorithms.InsStrategy
import algorithms.MISort
import algorithms.t

fun main(args: Array<String>) {

    val elements = (14..21).map { (t(it) *2-1).toBigInteger().intValueExact() }.toMutableList()
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)


    println("num_elements\t1.0\t1.02\t1.03\t1.04\t1.05")
    for (num_elements in elements) {
        print(num_elements)
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.LEFT) { k -> (t(k) * 1.0).toLong()} })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.LEFT) { k -> (t(k) * 1.02).toLong()} })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.LEFT) { k -> (t(k) * 1.03).toLong()} })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.LEFT) { k -> (t(k) * 1.04).toLong()} })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.LEFT) { k -> (t(k) * 1.05).toLong()} })
        println()
    }
    println()
    println()
}