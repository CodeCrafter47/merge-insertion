package experiments

import compute.AvgApprox
import algorithms.InsStrategy
import algorithms.MISort
import algorithms.t
import data_structures.toRational
import java.math.MathContext
import kotlin.math.log2

fun main(args: Array<String>) {
    val elements = (6..21).map { (t(it) *2-1).toInt() }.toMutableList()
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)
    insertMiddleValues(elements)

    println("num_elements\tapprox\tMI")
    for (num_elements in elements) {
        print(num_elements)
        print('\t')
        val comparisons = AvgApprox.T(num_elements.toRational()).toBigDecimal()
        print(((comparisons - num_elements.toBigDecimal() * log2(num_elements + .0).toBigDecimal())).divide(num_elements.toBigDecimal(), MathContext.DECIMAL128))
        print('\t')
        println(randomSort(num_elements, iteration_count(num_elements)) { n, c -> MISort(n, n.size(), c, InsStrategy.LEFT) })
    }
    println()
    println()
}