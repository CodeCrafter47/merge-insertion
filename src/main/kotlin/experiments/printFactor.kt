package experiments

import algorithms.InsStrategy
import algorithms.MISort
import algorithms.t

fun main(args: Array<String>) {

    println("factor\t16165\t17727\t18851\t19440\t20673\t21845\t22672\t23380\t24863\t26440")
    for (factor in ((950/5)..(1150/5)).map { it / 200.0 }) {
        print(factor)
        for (num_elements in listOf(16165, 17727, 18851, 19440, 20673, 21845, 22672, 23380, 24863, 26440)) {
            val avg = randomSort(num_elements, iteration_count(num_elements)) { n, c  -> MISort(n, n.size(), c, InsStrategy.LEFT) { k -> (t(k) * factor).toLong()} }
            print("\t$avg")

        }
        println()
    }
    println()
    println()
}