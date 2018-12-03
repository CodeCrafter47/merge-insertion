package algorithms

import data_structures.Vector

/**
 * Splits the input into two parts of size m and n, where m is optimal for MergeInsertion. Both parts are sorted
 * separately using MergeInsertion and the merged.
 */
fun<T> MergeHLASort(data: Vector<T>, size: Long, cmp: Comparator<T>, strategy: InsStrategy) {
    val m = optimumLessThan(size)
    val n = size - m

    val large = Vector<T>()
    val small = Vector<T>()

    for (i in 0 until m) {
        large.insertAt(i, data.removeAt(0))
    }

    for (i in 0 until n) {
        small.insertAt(i, data.removeAt(0))
    }

    MISort(large, m, cmp, strategy)
    MISort(small, n, cmp, strategy)

    // HLA merge
    hlaMerge(large, small, data, cmp, strategy)
}

/**
 * Splits the input into two parts of size m and n, where m is optimal for MergeInsertion. The larger part is sorted
 * using MergeInsertion, the smaller part by recursively calling this function. Then both results are merged.
 */
fun<T> MergeHLASortRecursive(data: Vector<T>, size: Long, cmp: Comparator<T>, strategy: InsStrategy) {
    val m = optimumLessThan(size)
    val n = size - m

    val large = Vector<T>()
    val small = Vector<T>()

    for (i in 0 until m) {
        large.insertAt(i, data.removeAt(0))
    }

    for (i in 0 until n) {
        small.insertAt(i, data.removeAt(0))
    }

    MISort(large, m, cmp, strategy)
    if (n <= 20) {
        MISort(small, n, cmp, strategy)
    } else {
        MergeHLASortRecursive(small, n, cmp, strategy)
    }

    // HLA merge
    hlaMerge(large, small, data, cmp, strategy)
}

/**
 * Splits the input into three parts of size m, n and o, where m and n are optimal for MergeInsertion. The parts of
 * length m and n are sorted using MergeInsertion, then merged using the Hwang-Lin Algorithm. Then the remaining o
 * elements are inserted using binary insertion.
 */
fun<T> MergeHLASortBinIns(data: Vector<T>, size: Long, cmp: Comparator<T>, strategy: InsStrategy) {
    val m = optimumLessThan(size)
    val n = optimumLessThan(size - m)
    val o = size - m - n

    val large = Vector<T>()
    val small = Vector<T>()
    val others = Vector<T>()

    for (i in 0 until m) {
        large.insertAt(i, data.removeAt(0))
    }

    for (i in 0 until n) {
        small.insertAt(i, data.removeAt(0))
    }

    for (i in 0 until o) {
        others.insertAt(i, data.removeAt(0))
    }

    MISort(large, m, cmp, strategy)
    MISort(small, n, cmp, strategy)

    // HLA merge
    hlaMerge(large, small, data, cmp, strategy)

    while (others.size() != 0L) {
        binaryInsert(data, 0, data.size(), others.removeAt(0), cmp, strategy)
    }
}

/**
 * Splits the input into three parts of size m, n and o, where m and n are optimal for MergeInsertion. The parts of
 * length m and n are sorted using MergeInsertion, then merged using the Hwang-Lin Algorithm. Then the remaining o
 * elements are inserted using 1-2-Insertion.
 */
fun<T> MergeHLASort12Insertion(data: Vector<T>, size: Long, cmp: Comparator<T>, strategy: InsStrategy) {
    val m = optimumLessThan(size)
    val n = optimumLessThan(size - m)
    val o = size - m - n

    val large = Vector<T>()
    val small = Vector<T>()
    val others = Vector<T>()

    for (i in 0 until m) {
        large.insertAt(i, data.removeAt(0))
    }

    for (i in 0 until n) {
        small.insertAt(i, data.removeAt(0))
    }

    for (i in 0 until o) {
        others.insertAt(i, data.removeAt(0))
    }

    MISort(large, m, cmp, strategy)
    MISort(small, n, cmp, strategy)

    // HLA merge
    hlaMerge(large, small, data, cmp, strategy)

    while (others.size() != 0L) {
        data.insertAt(data.size(), others.removeAt(0))
    }
    insertion12_improved(m + n, data, cmp)
}

/**
 * Splits the input into three parts of size m, n and o, where m and n=m/4 are optimal for MergeInsertion. The parts of
 * length m and n are sorted using MergeInsertion, then merged using {@link ftbiMerge}. Then the remaining o
 * elements are inserted using binary insertion.
 */
fun<T> MergeFTBISort(data: Vector<T>, size: Long, cmp: Comparator<T>, strategy: InsStrategy) {
    var k_max = 3
    var m: Long

    do {
        k_max++
        m = (1L shl (k_max + 2)) / 3 + 2 * (1L shl (k_max)) / 3
    } while (m < size)
    k_max--
    m = (1L shl (k_max + 2)) / 3
    val n = (1L shl (k_max)) / 3

    val large = Vector<T>()
    val small = Vector<T>()
    val others = Vector<T>()

    for (i in 0 until m) {
        large.insertAt(i, data.removeAt(0))
    }

    for (i in 0 until n) {
        small.insertAt(i, data.removeAt(0))
    }

    while (data.size() != 0L) {
        others.insertAt(0, data.removeAt(0))
    }

    MISort(large, m, cmp, strategy)
    MISort(small, n, cmp, strategy)

    // FTBI Merge
    ftbiMerge(large, small, cmp, data, strategy)

    while (others.size() != 0L) {
        binaryInsert(data, 0, data.size(), others.removeAt(0), cmp, strategy)
    }
}

internal fun optimumLessThan(n: Long): Long {
    if (n < 2)
        return 0L
    var k = 2
    var o1: Long
    var o2 = 2L
    do {
        o1 = o2
        o2 = (1L shl (k + 2)) / 3
        k++
    } while (o2 <= n)
    return o1
}