package algorithms

import data_structures.Vector

/**
 * Implementation of the Hwang-Lin Algorithm for merging two lists.
 */
fun<T> hlaMerge(large: Vector<T>, small: Vector<T>, result: Vector<T>, cmp: Comparator<T>, strategy: InsStrategy) {
    // HLA merge
    if (large.size() != 0L && small.size() != 0L) {
        var t = java.lang.Long.max(1, java.lang.Long.highestOneBit(large.size() / small.size()))
        while (large.size() != 0L && small.size() != 0L) {

            if (small.size() == 1L) {
                binaryInsert(large, 0, large.size(), small.removeAt(0), cmp, strategy)
                break
            }

            if (t > large.size()) {
                t = java.lang.Long.max(1, java.lang.Long.highestOneBit(large.size() / small.size()))
            }

            val element = small.get(0)
            if (cmp.compare(element, large.get(t - 1)) > 0) {
                for (i in 1..t) {
                    result.insertAt(result.size(), large.removeAt(0))
                }
            } else {
                binaryInsert(large, 0, t - 1, element, cmp, strategy)
                do {
                    val i = large.removeAt(0)
                    result.insertAt(result.size(), i)
                } while (i != element)
                small.removeAt(0)
            }
        }
    }

    while (large.size() != 0L) {
        result.insertAt(result.size(), large.removeAt(0))
    }

    while (small.size() != 0L) {
        result.insertAt(result.size(), small.removeAt(0))
    }
}