package experiments

import algorithms.InsStrategy
import algorithms.MISort
import algorithms.t

fun main(args: Array<String>) {
    val elements = (16..21).map { t(it).toInt() }.toMutableList()
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)

    println("num_elements\tcenter-left\tcenter-right\tleft\tright")
    for (num_elements in elements) {
        print(num_elements)
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.CENTER_LEFT) })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.CENTER_RIGHT) })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.LEFT) })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.RIGHT) })
        println()
    }
}