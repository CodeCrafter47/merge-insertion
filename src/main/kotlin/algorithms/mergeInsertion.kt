package algorithms

import data_structures.Vector
import kotlin.math.min

val t = { k: Int -> ((1L shl (k+1)) + (if (k % 2 == 0) 1 else -1)) / 3 }

/**
 * Implementation of MergeInsertion
 */
fun<T> MISort(indices: Vector<T>, size: Long, cmp: Comparator<T>, strategy: InsStrategy, t: (Int) -> Long = algorithms.t): Unit {
    if (size <= 1)
        return

    val half = size / 2

    for (a in 0 until half) {
        val b = a + half
        if (cmp.compare(indices.get(a), indices.get(b)) < 0) {
            // swap compute.a and b
            indices.insertAt(a, indices.removeAt(b))
            indices.insertAt(b, indices.removeAt(a + 1))
        }
    }

    // recurse
    val correspondance = (0 until half).map { Pair(indices.get(it), indices.removeAt(half)) }.toMap()
    MISort(indices, half, cmp, strategy, t)

    val a = Vector<T>()
    val b = Vector<T>()
    indices.forEachIndexed(half) { idx, element ->
        a.append(element)
        b.append(correspondance[element]!!)
    }
    if (size % 2 == 1L)
        b.append(indices.removeAt(half))

    indices.insertAt(0, b.get(0))

    if (b.size() > 1) {
        var k = 2

        var prev = 1L
        do {
            val tk = t(k)

            val m = min(tk, b.size())

            var upper = prev + m - 1

            for (i in m - 1 downTo prev) {
                binaryInsert(indices, 0, upper, b.get(i), cmp, strategy);
                while (indices.get(upper) != a.get(i - 1)) {
                    upper--
                }
            }
            prev = tk
            k++
        } while (tk < b.size())
    }
}