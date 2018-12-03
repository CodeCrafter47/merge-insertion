package compute

import data_structures.toRational

fun pYi(k:Int, i: Int, j: Int): Double {
    val tk = t(k).toBigInteger().intValueExact()
    val tk1 = t(k - 1).toBigInteger().intValueExact()
    return if (1 <= i && 1 <= tk - tk1 && 2*tk1 + i - 1 <= j && j <= (1 shl k) - 1) {
        (factorial(2 * tk - i - j - 1).toRational()/(1.toBigInteger() shl
                ((1 shl k) -j-1)).toRational()/ factorial(j - 2 * tk1 - i + 1) / factorial((1 shl k) - j - 1) *(1.toBigInteger() shl (tk - tk1 - i))* factorial(i + j) / factorial(2 * tk - 1) * factorial(tk - 1) / factorial(tk1 + i - 1)).toDouble()
    } else 0.0
}

fun main(args: Array<String>) {
    val k = 7
    val a = 1
    val b = ((t(k) - t(k - 1))/2).toBigInteger().intValueExact()
    val c = ((t(k) - t(k - 1))).toBigInteger().intValueExact()
    println("k=$k")
    println("j\tY$a\tY$b\tY$c")
    for (j in (2* t(k - 1).toBigInteger().intValueExact())..((1 shl k)-1)) {
        println("$j\t${pYi(k, a, j)}\t${pYi(k, b, j)}\t${pYi(k, c, j)}")
    }
}