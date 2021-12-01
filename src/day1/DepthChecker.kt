package day1

fun solvePuzzle1(input: List<String>) {
    val intInput = input.map { it.toInt() }

    val result = countIncreasing(intInput)

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val intInput = input.map { it.toInt() }

    val sumInput = slidingWindowSums(3, intInput)

    val result = countIncreasing(sumInput)

    println(result)
}

fun countIncreasing(input: List<Int>): Int {
    var count = 0
    for (index in 1 until input.size) {
        if (input[index] > input[index - 1]) {
            count++
        }
    }
    return count
}

fun slidingWindowSums(windowSize: Int, input: List<Int>): List<Int> {
    require(input.size >= windowSize) {
        "input must contain enough items to fill at least one window"
    }
    val sumList = mutableListOf<Int>()
    for (index in windowSize - 1 until input.size) {
        var sum = 0
        for (windowIndex in 0 until windowSize) {
            sum += input[index - windowIndex]
        }
        sumList.add(sum)
    }
    return sumList.toList()
}
