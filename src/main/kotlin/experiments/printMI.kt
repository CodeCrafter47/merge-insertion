package experiments

import algorithms.InsStrategy
import algorithms.MISort
import algorithms.t

fun main(args: Array<String>) {

    val elements = (13..15).map { (t(it) *2-1).toInt() }.toMutableList()
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)

    println("num_elements\tMI")
    for (num_elements in elements) {
        print(num_elements)
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.LEFT) })
        println()
    }
    println()
    println()
}