package algorithms

import data_structures.Vector

enum class InsStrategy {
    /**
     * Compares to the middle element, or in case of an odd number of elements to the one left to the middle.
     */
    CENTER_LEFT,
    /**
     * Compares to the middle element, or in case of an odd number of elements to the one right to the middle.
     */
    CENTER_RIGHT,
    /**
     * Chooses the element to compare with as the left-most, such that the resulting decision tree has its leaves spread across at most two layers.
     */
    LEFT,
    /**
     * Chooses the element to compare with as the right-most, such that the resulting decision tree has its leaves spread across at most two layers.
     */
    RIGHT
}

/**
 * Inserts an element into compute.a sorted list of elements using binary insertion with the selected strategy.
 *
 * @param data sorted list of elements
 * @param lower lower bound, the function expects @code{data[lower - 1] < element}
 * @param size provides the upper bound together with @code{lower}, i.e. the function expects @code{element < data[lower + size]]}
 * @param element the element to be inserted
 * @param cmp the comparator to be used
 * @param strategy the desired strategy for binary insertion
 */
fun <T> binaryInsert(data: Vector<T>, lower: Long, size: Long, element: T, cmp: Comparator<T>, strategy: InsStrategy) {
    if (size == 0L) {
        data.insertAt(lower, element)
    } else {
        val cmpIdx = when (strategy) {
            InsStrategy.CENTER_LEFT -> lower + (size - 1) / 2
            InsStrategy.CENTER_RIGHT -> lower + (size) / 2
            InsStrategy.LEFT -> {
                val gp = java.lang.Long.highestOneBit(size)
                if (size == 1L) {
                    lower
                } else if (size - gp + 1 >= gp / 2) {
                    lower + size - gp
                } else {
                    lower + gp / 2 - 1
                }
            }
            InsStrategy.RIGHT -> {
                val gp = java.lang.Long.highestOneBit(size)
                if (size == 1L) {
                    lower;
                } else if (size - gp + 1 >= gp / 2) {
                    lower + gp - 1;
                } else {
                    lower + size - gp / 2;
                }
            }
        }
        if (cmp.compare(data.get(cmpIdx), element) > 0) {
            binaryInsert(data, lower, cmpIdx - lower, element, cmp, strategy)
        } else {
            binaryInsert(data, cmpIdx + 1, lower + size - cmpIdx - 1, element, cmp, strategy)
        }
    }
}
