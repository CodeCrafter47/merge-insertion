package experiments

import algorithms.*
import experiments.insertMiddleValues
import experiments.iteration_count
import experiments.randomSort


fun main(args: Array<String>) {
    println("num_elements\tMI\tMI/wMerge\tMI/wMergeBinIns\tMI/wMerge12Ins\t1-2-Insertion\t1-2-Insertion-Improved\t1-2-Insertion-Combined")

    val elements = mutableListOf(87381, 174763)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    elements.add(174763 + (174763 - 87381) / 4)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)

    for (num_elements in elements) {
        print(num_elements)
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.LEFT) })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MergeHLASort(n, n.size(), c, InsStrategy.LEFT) })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MergeHLASortBinIns(n, n.size(), c, InsStrategy.LEFT) })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MergeHLASort12Insertion(n, n.size(), c, InsStrategy.LEFT) })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> insertion12(n, c) })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> insertion12_improved(n, c) })
        print('\t')
        print(randomSort(num_elements, iteration_count(num_elements)) { n, c -> insertion12_combined(n, c) })
        println()
    }
}
