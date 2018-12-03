package compute

import compute.t

fun main(args: Array<String>) {
    for ( k in 1..20)
        println("t_$k = ${t(k)}")
}