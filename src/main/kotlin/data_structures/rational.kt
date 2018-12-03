package data_structures

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

class Rational {
    val m: BigInteger
    val n: BigInteger

    constructor(m: BigInteger, n: BigInteger) {
        if (m == BigInteger.ZERO) {
            this.m = BigInteger.ZERO
            this.n = BigInteger.ONE
        } else {
            val d = gcd(if (m.signum() < 0) -m else m, if (n.signum() < 0) -n else n)
            if (n.signum() < 0) {
                this.m = -m / d
                this.n = -n / d
            } else {
                this.m = m / d
                this.n = n / d
            }
        }
    }

    private fun gcd(a: BigInteger, b: BigInteger): BigInteger {
        val A = if (a > b) a else b
        val B = if (a > b) b else a

        if (B == BigInteger.ZERO)
            return A

        return gcd(b, a % b)
    }

    private fun gcm(a: BigInteger, b: BigInteger): BigInteger {
        val g = gcd(a, b)

        return a * (b / g)
    }

    operator fun plus(other: Rational): Rational {
        val n2 = gcm(n, other.n)

        return Rational(m * (n2 / n) + other.m * (n2 / other.n), n2)
    }

    operator fun plus(other: Int): Rational {
        return this + other.toRational()
    }

    operator fun minus(other: Rational): Rational {
        val n2 = gcm(n, other.n)

        return Rational(m * (n2 / n) - other.m * (n2 / other.n), n2)
    }

    operator fun minus(other: Int): Rational {
        return this - other.toRational()
    }

    operator fun times(other: Rational): Rational {
        return Rational(m * other.m, n * other.n)
    }

    operator fun times(other: Int): Rational {
        return this * other.toRational()
    }

    operator fun times(other: BigInteger): Rational {
        return this * other.toRational()
    }

    operator fun div(other: Rational): Rational {
        return Rational(m * other.n, n * other.m)
    }

    operator fun div(other: Int): Rational {
        return this / other.toRational()
    }

    operator fun compareTo(other: Rational): Int {
        val n2 = gcm(n, other.n)

        return (m * (n2 / n)).compareTo(other.m * (n2 / other.n))
    }

    operator fun compareTo(other: Int): Int {
        return this.compareTo(other.toRational())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rational

        if (m != other.m) return false
        if (n != other.n) return false

        return true
    }

    override fun hashCode(): Int {
        var result = m.hashCode()
        result = 31 * result + n.hashCode()
        return result
    }

    override fun toString(): String {
        return if (n == BigInteger.ONE)
            m.toString()
        else
            (m.toBigDecimal().divide(n.toBigDecimal(), MathContext.DECIMAL128)).toString()
    }

    fun toDouble(): Double {
        return if (n == BigInteger.ONE)
            m.toDouble()
        else
            m.toDouble() / n.toDouble()
    }

    fun toBigDecimal(): BigDecimal {
        return m.toBigDecimal().divide(n.toBigDecimal(), MathContext.DECIMAL128)
    }

    fun toBigInteger(): BigInteger {
        if (n != BigInteger.ONE) {
            throw AssertionError("m != 1")
        }
        return m
    }

    fun toStringFrac(): String {
        return if (m == BigInteger.ZERO) "0" else "$m/$n"
    }

    fun toStringPrimes(): String {

        fun primes (num: BigInteger): String {
            var n = num
            val result = ArrayList<BigInteger>(16)

            while (!n.testBit(0)) {
                result += 2.toBigInteger()
                n = n / 2.toBigInteger()
            }

            var i = 3.toBigInteger()
            while (i * i < n) {
                if (n % i == 0.toBigInteger()) {
                    result += i
                    n = n / i
                } else {
                    i++
                }
            }

            if (n != 1.toBigInteger() || result.isEmpty()) {
                result.add(n)
            }

            return result.joinToString(separator = "*") { it.toString() }
        }
        return if (m == BigInteger.ZERO) "0" else "${primes(m)}/${primes(n)}"
    }

    operator fun div(other: BigInteger): Rational {
        return div(other.toRational())
    }
}

fun floor(r: Rational): Rational {
    return Rational(r.m / r.n, BigInteger.ONE)
}

fun ceil(r: Rational): Rational {
    return Rational((r.m + r.n - BigInteger.ONE) / r.n, BigInteger.ONE)
}

fun Int.toRational(): Rational {
    return Rational(BigInteger.valueOf(this.toLong()), BigInteger.ONE)
}

fun Long.toRational(): Rational {
    return Rational(BigInteger.valueOf(this), BigInteger.ONE)
}

fun BigInteger.toRational(): Rational {
    return Rational(this, BigInteger.ONE)
}

operator fun Int.times(other: Rational): Rational {
    return this.toRational() * other
}