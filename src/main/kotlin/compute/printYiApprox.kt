package compute

import kotlin.math.*

fun pYiApprox(k: Int, i: Int, j: Int): Double {
    val tk = t(k).toBigInteger().intValueExact()
    val tk1 = t(k - 1).toBigInteger().intValueExact()
    return if (1 <= i && 1 <= tk - tk1 && 2 * tk1 + i - 1 <= j && j <= (1 shl k) - 1) {
        binom((ceil(i / 2.0).toInt()).toBigInteger(), ((1 shl k) - 1 - j).toBigInteger()).toDouble() *
                ((floor(i / 2.0) / (2*tk-1)).pow((1 shl k) -1 - j)) *
                ((((2*tk-1) - floor(i / 2.0)) / (2*tk-1)).pow(ceil(i/2.0).toInt() - ((1 shl k) -1 - j)))
    } else 0.0
}

fun main(args: Array<String>) {
    val k = 8
    val i = ((t(k) - t(k - 1))/2).toBigInteger().intValueExact()
    println("j\tY$i\tY${i}Appr")
    for (j in (2* t(k - 1).toBigInteger().intValueExact())..((1 shl k)-1)) {
        println("$j\t${pYi(k, i, j)}\t${pYiApprox(k, i, j)}")
    }
}