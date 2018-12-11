package algorithms

import data_structures.Vector

/*
 * This file contains implementations of algorithms described by Iwama and Teruyama in <a href="https://doi.org/10.1007/978-3-319-62127-2_41">https://doi.org/10.1007/978-3-319-62127-2_41</a>
 */

/**
 * 1-2-Insertion sort.
 *
 * Sorts the data by repeatedly inserting one element or two elements at once.
 */
fun <T> insertion12(data: Vector<T>, cmp: Comparator<T>) {
    if (cmp.compare(data.get(0), data.get(1)) > 0) {
        data.insertAt(0, data.removeAt(1))
    }
    var sorted = 2L
    while (sorted != data.size()) {
        val pn = (sorted + .0) / ((2 * java.lang.Long.highestOneBit(sorted - 1)) + .0)
        if (pn in 0.5511..0.888 && sorted % 2 == 0L && data.size() - sorted >= 2) {
            merge2(data, 0, sorted, data.removeAt(sorted), data.removeAt(sorted), cmp)
            sorted += 2
        } else if (data.size() - sorted >= 2 && sorted % 2 == 0L) {
            RHBS(data, 0, sorted, data.removeAt(sorted), cmp)
            sorted++
            RHBS(data, 0, sorted, data.removeAt(sorted), cmp)
            sorted++
        } else {
            RHBS(data, 0, sorted, data.removeAt(sorted), cmp)
            sorted++
        }
    }
}

/**
 * Improved variant of 1-2-Insertion sort.
 *
 * This used the improved 2Merge algorithm.
 */
fun <T> insertion12_improved(data: Vector<T>, cmp: Comparator<T>) {
    if (cmp.compare(data.get(0), data.get(1)) > 0) {
        data.insertAt(0, data.removeAt(1))
    }
    val sorted = 2L
    insertion12_improved(sorted, data, cmp)
}

fun <T> insertion12_improved(sorted: Long, data: Vector<T>, cmp: Comparator<T>) {
    var sorted1 = sorted
    while (sorted1 != data.size()) {
        val pn = (sorted1 + .0) / ((2 * java.lang.Long.highestOneBit(sorted1 - 1)) + .0)
        if (pn in 0.5511..0.888 && sorted1 % 2 == 0L && data.size() - sorted1 >= 2) {
            merge2_improved(data, 0, sorted1, data.removeAt(sorted1), data.removeAt(sorted1), cmp)
            sorted1 += 2
        } else if (data.size() - sorted1 >= 2 && sorted1 % 2 == 0L) {
            RHBS(data, 0, sorted1, data.removeAt(sorted1), cmp)
            sorted1++
            RHBS(data, 0, sorted1, data.removeAt(sorted1), cmp)
            sorted1++
        } else {
            RHBS(data, 0, sorted1, data.removeAt(sorted1), cmp)
            sorted1++
        }
    }
}

/**
 * Combination of 1-2-Insertion with MergeInsertion.
 */
fun<T> insertion12_combined(data: Vector<T>, cmp: Comparator<T>, t: (Int) -> Long = algorithms.t) {
    val pn = (data.size() + .0) / ((2 * java.lang.Long.highestOneBit(data.size() - 1)) + .0)
    val n = if (pn >= 2.0 / 3.0)
        (2 * data.size() / 3 / pn).toLong()
    else
        (data.size() / 3 / pn).toLong()
    MISort(data, n, cmp, InsStrategy.LEFT, t)
    insertion12_improved(n, data, cmp)
}

/**
 * Binary Insertion
 */
internal fun <T> RHBS(data: Vector<T>, lower: Long, size: Long, element: T, cmp: Comparator<T>): Long {
    if (size == 0L) {
        data.insertAt(lower, element)
        return lower
    } else {
        val i = size
        val tmp = java.lang.Long.highestOneBit(i) / 2
        val d = if (i <= 3 * tmp - 1)
            tmp
        else
            i - java.lang.Long.highestOneBit(i) + 1
        if (d <= 0)
            throw AssertionError("d <= 0")
        if (d > size)
            throw AssertionError("d > size")

        if (cmp.compare(data.get(lower + d - 1), element) > 0) {
            return RHBS(data, lower, d - 1, element, cmp)
        } else {
            return RHBS(data, lower + d, size - d, element, cmp)
        }
    }
}

/**
 * Implementation of 2Merge. Inserts two elements at once.
 */
private fun <T> merge2(data: Vector<T>, lower: Long, size: Long, a: T, b: T, cmp: Comparator<T>) {
    if (size % 2 != 0L) {
        throw AssertionError("size must be even")
    }
    if (size < 2) {
        throw AssertionError("size must be at least 2")
    }

    val B: T
    val A: T
    if (cmp.compare(a, b) > 0) {
        B = a
        A = b
    } else {
        B = b
        A = a
    }

    val i = size + 2

    var largestIdxSmallerThanA = -1L
    var d = 0L
    var r = 1
    do {
        largestIdxSmallerThanA = d - 1
        d = i - (i / Math.sqrt(Math.pow(2.0, r + .0))).toInt()
        if (d - 1 <= largestIdxSmallerThanA) {
            // todo throw AssertionError("d <= largestIdxSmallerThanA")
            d = largestIdxSmallerThanA + 2
        }
        if (d > size) {
            d = size + 1
        }
        r++
    } while (d <= size && cmp.compare(A, data.get(lower + d - 1)) > 0)

    val l = RHBS(data, lower + largestIdxSmallerThanA + 1, d - largestIdxSmallerThanA - 2, A, cmp)
    RHBS(data, l + 1, size - l + lower, B, cmp)
}

/**
 * Improved version of 2Merge
 */
private fun <T> merge2_improved(data: Vector<T>, lower: Long, size: Long, a: T, b: T, cmp: Comparator<T>) {
    if (size % 2 != 0L) {
        throw AssertionError("size must be even")
    }
    if (size < 2) {
        throw AssertionError("size must be at least 2")
    }

    val B: T
    val A: T
    if (cmp.compare(a, b) > 0) {
        B = a
        A = b
    } else {
        B = b
        A = a
    }

    val i = size + 2

    var largestIdxSmallerThanA = -1L
    var d = 0L
    var r = 1
    do {
        largestIdxSmallerThanA = d - 1
        val pi = (i + .0) / ((2 * java.lang.Long.highestOneBit(i - 1)) + .0)
        d = when {
            r % 2 == 0 -> i - i / (1 shl (r / 2))
            pi > 0.75 -> i - i / (1 shl (r / 2)) + (i / pi / (1 shl (r / 2 + 2))).toInt()
            else -> i - i / (1 shl (r / 2 + 1)) - (i / pi / (1 shl (r / 2 + 3))).toInt()
        }
        if (d - 1 <= largestIdxSmallerThanA) {
            // todo throw AssertionError("d <= largestIdxSmallerThanA")
            d = largestIdxSmallerThanA + 2
        }
        if (d > size) {
            d = size + 1
        }
        r++
    } while (d <= size && cmp.compare(A, data.get(lower + d - 1)) > 0)

    val l = RHBS(data, lower + largestIdxSmallerThanA + 1, d - largestIdxSmallerThanA - 2, A, cmp)
    RHBS(data, l + 1, size - l + lower, B, cmp)
}