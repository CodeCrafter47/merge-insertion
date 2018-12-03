package compute

import data_structures.times
import data_structures.toRational

/**
 * Calculates various probabilities in play when inserting one batch of elements during the insertion step of
 * MergeInsertion. Creates fancy ascii art.
 */
fun main(args: Array<String>) {
    for (k in 2..4)
        printLinExp(k)
}

fun printLinExp(k: Int) {
    println("k = $k")

    val main_chain = (1..((t(k - 1) * 2).toBigInteger().intValueExact())).map { "x$it" }
    val a_chain = (((t(k - 1) + 1).toBigInteger().intValueExact())..(t(k).toBigInteger().intValueExact())).map { "a$it" }
    val b_chain = (((t(k - 1) + 1).toBigInteger().intValueExact())..(t(k).toBigInteger().intValueExact())).map { "b$it" }
    val chain = main_chain + a_chain

    println(chain.joinToString(separator = " -> "))
    println(" ".repeat(main_chain.joinToString(separator = " -> ").length + 4) + "^     ".repeat(a_chain.size))
    println(" ".repeat(main_chain.joinToString(separator = " -> ").length + 4) + "|     ".repeat(a_chain.size))
    println(b_chain.joinToString(separator = "    ", prefix = " ".repeat(main_chain.joinToString(separator = " -> ").length + 3)))

    println()

    var count = 0L
    val countIndividual = Array(b_chain.size) { LongArray(chain.size) { 0 } }
    val countIndex = Array(b_chain.size) { LongArray(chain.size) { 0 } }
    val countLength = Array(b_chain.size) { LongArray(chain.size + 1) { 0 } }
    val countIndexByLength = Array(b_chain.size) { Array(chain.size + 1) { LongArray(chain.size) { 0 } } }

    fun ins(dest_chain: List<String>, src_idx: Int) {
        if (src_idx == b_chain.size) {
            count++
            var idx = 0
            for (element in dest_chain) {
                if (element in b_chain) {
                }
                if (idx < chain.size && element == chain[idx]) {
                    idx++
                } else {
                    countIndividual[b_chain.indexOf(element)][idx]++
                }
            }
            for (a in a_chain) {
                val aIdxInDest = dest_chain.indexOf(a)
                val aIdxInA = a_chain.indexOf(a)
                val chainAfterIns = dest_chain.filterIndexed { index, e -> index < aIdxInDest && (e !in b_chain || b_chain.indexOf(e) >= aIdxInA) }
                val bIdxAfterIns = chainAfterIns.indexOf(b_chain[aIdxInA])
                countLength[aIdxInA][chainAfterIns.size - 1]++
                countIndexByLength[aIdxInA][chainAfterIns.size - 1][bIdxAfterIns]++
                countIndex[aIdxInA][bIdxAfterIns]++
            }
        } else {
            val element = b_chain[src_idx]
            for (i in 0..(dest_chain.size)) {
                ins(dest_chain.take(i) + element + dest_chain.drop(i) + a_chain[src_idx], src_idx + 1)
            }
        }
    }
    ins(main_chain, 0)

    println("e\tP(e<x0)\t" + (0..(chain.size - 2)).joinToString(separator = "\t") { "P(${chain[it]}<e<${chain[it + 1]})" })
    for (i in countIndividual.indices.reversed()) {
        println("${b_chain[i]}\t" + countIndividual[i].joinToString(separator = "\t") { (it.toRational() / count.toRational()).toStringFrac() })
        val j = countIndividual.size - i - 1
        println("${b_chain[i]}(f)\t" + (0..(2 * t(k - 1) + i).toBigInteger().intValueExact()).joinToString(separator = "\t")
        { i ->
            val range1 = j..Math.min((t(k) - t(k - 1) -1 ).toBigInteger().intValueExact(), (1 shl k) - i - 1)
            val range2 = (j + 1)..Math.min((t(k) - t(k - 1) -1 ).toBigInteger().intValueExact(), (1 shl k) - i - 1)
            (
                    ((if (range1.isEmpty()) 0.toRational() else range1.map { 1.toRational() / (2 * (t(k) - it) - 1) }.reduce { a, b -> a * b })) *
                            (if (range2.isEmpty()) 1.toRational() else range2.map { (2 * (t(k) - it)) }.reduce { a, b -> a * b })
                    ).toStringFrac()
        })
    }
    println()

    val posRange = chain.indices
    println("e|pos\t" + posRange.joinToString(separator = "\t"))
    for ((index, values) in countIndex.withIndex().reversed()) {
        println("${b_chain[index]}\t" + values.joinToString(separator = "\t") { (it.toRational() / count.toRational()).toString() })
    }
    println()

    val lengthRange = ((t(k - 1) * 2).toBigInteger().intValueExact()) until chain.size
    println("e|into\t" + lengthRange.joinToString(separator = "\t"))
    for ((index, values) in countLength.withIndex().reversed()) {
        println("${b_chain[index]}\t" + lengthRange.map { values[it] }.joinToString(separator = "\t") { (it.toRational() / count.toRational()).toString() })
    }
    println()

    println("e(into)|pos\t" + posRange.joinToString(separator = "\t"))
    for ((index, posCount) in countIndexByLength.withIndex().reversed()) {
        val lengthRange = ((t(k - 1) * 2).toBigInteger().intValueExact() + index) until chain.size
        for ((into, values) in lengthRange.reversed().map { it to posCount[it] }) {
            println("${b_chain[index]}($into)\t" + values.joinToString(separator = "\t") { (it.toRational() / countLength[index][into].toRational()).toStringFrac() })
        }
    }

    println()
    println()
}