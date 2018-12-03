package algorithms

import data_structures.Vector

/**
 * Implementation of the merging algorithm presented by C. Christen in <a href="https://doi.org/10.1109/SFCS.1978.20">https://doi.org/10.1109/SFCS.1978.20</a>.
 */
// TODO this doesn't work :(
fun<T> ftbiMerge(large: Vector<T>, small: Vector<T>, cmp: Comparator<T>, result: Vector<T>, strategy: InsStrategy) {
    val j = Integer.numberOfTrailingZeros(java.lang.Long.highestOneBit(1 + large.size() / small.size()).toInt()) / 2
    val k = Integer.max(0, java.lang.Long.numberOfTrailingZeros(java.lang.Long.highestOneBit(large.size() / small.size())) - 2 * j)
    fun T(i: Long) = (1L shl (2 * (j + 1))) + (i - 1) * ((1L shl (2 * (j + 1))) - 1) / 3

    if (small.size() == 1L) {
        while (large.size() != 0L) {
            result.insertAt(result.size(), large.removeAt(0))
        }
        binaryInsert(result, 0, result.size(), small.removeAt(0), cmp, strategy)
    } else if (j == 0 || (1 shl k) * T(1) > large.size()) {
        hlaMerge(large, small, result, cmp, strategy)
    } else {
        var i = 1L
        while (i <= small.size() && cmp.compare(small.get(i-1), large.get((1L shl k) * T(i) - 1)) < 0) {
            i++
        }
        val x = --i

        // Now A_i < B_(2^k*T(i)) for i = 1..x
        val insertions = ArrayList<Pair<T,Long>>()
        while (i > 0 && cmp.compare(small.get(i - 1), large.get((1 shl k) * T(i) - (1L shl (2 * j + k + 1)) - 1)) > 0) {
            val pos = RHBS(large, (1 shl k) * T(i) - (1L shl (2 * j + k + 1)), (1L shl (2 * j + k + 1)) - 1, small.get(i - 1), cmp)
            large.removeAt(pos)
            insertions.add(Pair(small.get(i-1), pos))
            i--
        }
        val y = i

        // Now Ay < B_(2^k*T(i)-2^(2j+k+1))

        // Remove upper for recursion
        val smallUpper = Vector<T>()
        while (small.size() > x) {
            smallUpper.insertAt(smallUpper.size(), small.removeAt(x))
        }

        val largeUpper = Vector<T>()
        while (large.size() > (1 shl k) * T(x + 1)) {
            largeUpper.insertAt(largeUpper.size(), large.removeAt((1 shl k) * T(x + 1)))
        }

        // Insert middle parts
        insertions.sortByDescending { it.second }
        for ((element, pos) in insertions) {
            large.insertAt(pos, element)
        }

        while (small.size() > y) {
            small.removeAt(y)
        }

        val idx = (1 shl k) * T(y) - (1 shl (2 * j + k + 1)) - 1
        while (large.size() > idx) {
            result.insertAt(result.size(), large.removeAt(idx))
        }

        if (small.size() == 0L) {
            while (large.size() != 0L) {
                result.insertAt(0, large.removeAt(large.size() - 1))
            }
        } else if (large.size() == 0L) {
            while (small.size() != 0L) {
                result.insertAt(0, small.removeAt(small.size() - 1))
            }
        } else {
            val resultL = Vector<T>()
            ftbiMerge(large, small, cmp, resultL, strategy)
            while (resultL.size() != 0L) {
                result.insertAt(0, resultL.removeAt(resultL.size() - 1))
            }
        }

        if (smallUpper.size() == 0L) {
            while (largeUpper.size() != 0L) {
                result.insertAt(result.size(), largeUpper.removeAt(0))
            }
        } else if (largeUpper.size() == 0L) {
            while (smallUpper.size() != 0L) {
                result.insertAt(result.size(), smallUpper.removeAt(0))
            }
        } else {
            val resultR = Vector<T>()
            ftbiMerge(largeUpper, smallUpper, cmp, resultR, strategy)
            while (resultR.size() != 0L) {
                result.insertAt(result.size(), resultR.removeAt(0))
            }
        }
    }
}