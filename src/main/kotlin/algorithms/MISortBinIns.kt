package algorithms

import data_structures.Vector

fun<T> MISortBinIns(data: Vector<T>, size: Long, cmp: Comparator<T>, strategy: InsStrategy) {
    val n = optimumLessThan(size)
    MISort(data, n, cmp, strategy)

    for (i in n until size) {
        binaryInsert(data, 0, i, data.removeAt(i), cmp, InsStrategy.LEFT);
    }
}