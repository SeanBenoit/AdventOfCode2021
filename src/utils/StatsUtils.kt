package utils

import java.math.BigInteger

fun Collection<Int>.median(): Int {
    val sortedList = this.sorted()

    val midPoint = this.size / 2
    return if (this.size % 2 == 0) {
        (sortedList[midPoint - 1] + sortedList[midPoint]) / 2
    } else {
        sortedList[midPoint]
    }
}

fun Collection<BigInteger>.median(): BigInteger {
    val sortedList = this.sorted()

    val midPoint = this.size / 2
    return if (this.size % 2 == 0) {
        (sortedList[midPoint - 1] + sortedList[midPoint]) / 2.toBigInteger()
    } else {
        sortedList[midPoint]
    }
}
