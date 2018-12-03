package compute

fun eYi(k: Int, i: Int): Double {
    return ((2* t(k - 1).toBigInteger().intValueExact() + i - 1)..((1 shl k)-1))
            .map { j -> pYi(k, i, j) * j}
            .sum()
}

fun main(args: Array<String>) {
    val k = 7
    println("k=$k")
    println("i\tEYi")
    for (i in 1..((t(k) - t(k - 1)).toBigInteger().intValueExact())) {
        println("$i\t${eYi(k, i)}")
    }
}