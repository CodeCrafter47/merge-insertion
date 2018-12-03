package compute

import data_structures.*
import java.math.BigInteger

private val ZERO = 0.toRational()
private val ONE = 1.toRational()
private val TWO = 2.toRational()

abstract class Base {
    fun T(n: Rational): Rational {
        return if (n == ONE)
            ZERO
        else {
            floor(n / TWO) + T(floor(n / TWO)) + TI(ceil(n / TWO))
        }
    }

    fun TI(m: Rational): Rational {
        var result = ZERO
        if (m <= ONE)
            return result
        var k = 2
        while (m > t(k)) {
            result += BIFull(k)
            k++
        }
        return result + BILast(k, m)
    }

    abstract fun BIFull(k: Int): Rational

    abstract fun BILast(k: Int, m: Rational): Rational
}

/**
 * Worst case of MergeInsertion
 */
object WorstCase : Base() {
    override fun BIFull(k: Int): Rational {
        return k.toRational() * (t(k) - t(k - 1))
    }

    override fun BILast(k: Int, m: Rational): Rational {
        return k.toRational() * (m - t(k - 1))
    }
}

/**
 * Upper bound for the average-case as calculated by Edelkamp and Weiss <a href="https://arxiv.org/abs/1307.3033">https://arxiv.org/abs/1307.3033</a>
 */
object AvgCase : Base() {
    override fun BIFull(k: Int): Rational {
        var count = 0.toRational()
        var totalDepth = 0.toRational()

        fun BIns(e: Rational, lower: Int, upper: Int, depth: Int) {
            if (lower == upper) {
                if (e == 1.toRational()) {
                    count += 1.toRational()
                    totalDepth += depth.toRational()
                } else {
                    BIns(e - 1, 0, (1 shl k) - 1, depth)
                }
            } else {
                val middle = ((lower + upper) / 2)
                BIns(e, lower, middle, depth + 1)
                BIns(e, middle + 1, upper, depth + 1)
            }
        }

        BIns(t(k) - t(k - 1), 0, (1 shl k) - 1, 0)

        return totalDepth / count
    }

    override fun BILast(k: Int, m: Rational): Rational {
        var count = 0.toRational()
        var totalDepth = 0.toRational()

        fun BIns(e: Rational, lower: Rational, upper: Rational, depth: Int) {
            if (lower == upper) {
                if (e == 1.toRational()) {
                    count += 1.toRational()
                    totalDepth += depth.toRational()
                } else {
                    BIns(e - 1, ZERO, (1 shl k).toRational() - 1 - t(k) + m, depth)
                }
            } else {
                val middle = floor((lower + upper) / 2)
                BIns(e, lower, middle, depth + 1)
                BIns(e, middle + 1, upper, depth + 1)
            }
        }

        BIns(m - t(k - 1), ZERO, (1 shl k).toRational() - 1 - t(k) + m, 0)

        return totalDepth / count
    }

}


private data class r(val children: BigInteger, val cost: BigInteger)

private data class key(val k: Int, val current: Int, val main_chain: Int, val a_chain: List<Int>)
private val ins_cache: MutableMap<key, r> = HashMap()

/**
 * Exact cost of the average case
 */
object AvgCaseExact : Base() {
    override fun BIFull(k: Int): Rational {
        return BILast(k, t(k))
    }

    private fun ins(k: Int, current: Int, main_chain: Int, a_chain: List<Int>): r = ins_cache.getOrPut(key(k, current, main_chain, a_chain)) {
        assert(a_chain.size == current)
        val elements = main_chain + a_chain.sum()
        val cheapInserts = (1 shl k) - elements - 1
        val expensiveInserts = elements + 1 - cheapInserts

        var children = 0.toBigInteger()
        var cost = 0.toBigInteger()

        val recurse: (Int, Int, Int, List<Int>) -> r = if (current == 0) { _, _, _, _ -> r(1.toBigInteger(), 0.toBigInteger()) } else { k, c, m, a -> ins(k, c, m, a) }
        val child1 = recurse(k, current - 1, main_chain + 1, a_chain.dropLast(1))

        for (i in 0..elements) {
            val bSearchCost = if (i < cheapInserts) k - 1 else k

            if (i <= main_chain) {
                children += child1.children
                cost += bSearchCost.toBigInteger() * child1.children + child1.cost
            } else {
                var ind = i - main_chain - 1
                var indr = 0
                while (ind - a_chain[indr] >= 0) {
                    ind -= a_chain[indr]
                    indr ++
                }

                val newAList = a_chain.toMutableList()
                newAList[indr] += 1
                val child = recurse(k, current - 1, main_chain, newAList.dropLast(1))
                children += child.children
                cost += bSearchCost.toBigInteger() * child.children + child.cost
            }
        }

        return@getOrPut r(children, cost)
    }

    override fun BILast(k: Int, m: Rational): Rational {
        val elements = m.m.toInt() - t(k - 1).m.toInt()
        val c = ins(k, elements - 1, 2 * t(k - 1).m.toInt(), (0..(elements - 2)).map({ 1 }))
        return Rational(c.cost, 1.toBigInteger()) / Rational(c.children, 1.toBigInteger())
    }

}

/**
 * Upper bound for the average case as described in my bachelor thesis
 */
object AvgApprox : Base() {
    override fun BIFull(k: Int): Rational {
        return BILast(k, t(k))
    }

    val cache = HashMap<Triple<Int, Int, Int>, Rational>()

    override fun BILast(k: Int, m: Rational): Rational {
        var sum = 0.toRational()
        for (i in 0 until (m - t(k - 1)).toBigInteger().intValueExact()) {
            sum += TIns(i, m, k)
        }
        return sum
    }

    private fun TIns(i: Int, m: Rational, k: Int): Rational {
        if (cache.containsKey(Triple(k, m.toBigInteger().intValueExact(), i))) {
            return cache.get(Triple(k, m.toBigInteger().intValueExact(), i))!!
        }
        var s = 0.toRational()
        for (j in 0..i) {

            val p = a(i, j).toRational() *
                    (1.toBigInteger() shl i) *
                    factorial(2 * m - i - j - 1) / factorial(2 * m - 1) *
                    factorial(m - 1) / factorial(m - i - 1)

            val elements = t(k - 1) + m - 1 - j
            val cheapInserts = (1.toBigInteger() shl k).toRational() - elements - 1
            val expensiveInserts = elements + 1 - cheapInserts

            val t = p * (cheapInserts * (k - 1) + expensiveInserts * k) / (elements + 1)
            s += t
        }
        cache.put(Triple(k, m.toBigInteger().intValueExact(), i), s)
        return s
    }

}

fun main(args: Array<String>) {
    println("n\tworst\tavg\tavg*n!")
    var n = 3
    while (true) {
        val worst = WorstCase.T(n.toRational())
        //val avrg1 = compute.AvgCase.T(n.toRational())
        val avrg = AvgCaseExact.T(n.toRational())
        //val approx1 = compute.AvgApprox.T(n.toRational())
        println("$n\t$worst\t$avrg\t${avrg.times(factorial(n))}")
        n++
    }
}