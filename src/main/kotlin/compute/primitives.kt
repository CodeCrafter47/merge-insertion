package compute

import data_structures.Rational
import data_structures.toRational
import java.math.BigInteger

fun t(k: Int): Rational {
    if (k < 1)
        return 1.toRational()
    else
        return ((1.toBigInteger() shl (k+1)) + (if (k%2 == 0) 1 else -1).toBigInteger()).toRational() / 3
}

fun factorial(i: Int): BigInteger {
    var r = 1.toBigInteger()
    for (j in 2..i) {
        r *= j.toBigInteger()
    }
    return r
}

fun factorial(i: Rational): Rational {
    return factorial(i.toBigInteger()).toRational()
}

fun factorial(i: BigInteger): BigInteger {
    var r = 1.toBigInteger()
    var j = 2.toBigInteger()
    while (j <= i) {
        r *= j
        j++
    }
    return r
}

fun binom(n: BigInteger, k: BigInteger): BigInteger {
    return factorial(n) /(factorial(k) * factorial(n - k))
}

fun a(i: Int, j: Int) =
        factorial(i + j) / ((1.toBigInteger() shl j) * factorial(i - j) * factorial(j))