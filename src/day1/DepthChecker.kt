package day1

fun solvePuzzle1(input: List<String>) {
    val intInput = input.map { it.toInt() }
    var count = 0
    for (index in 1 until intInput.size) {
        if (intInput[index] > intInput[index - 1]) {
            count++
        }
    }
    println(count)
}

fun solvePuzzle2(input: List<String>) {
    val intInput = input.map { it.toInt() }
    var count = 0
    for (index in 3 until intInput.size) {
        val firstSum = intInput[index - 3] + intInput[index - 2] + intInput[index - 1]
        val secondSum = firstSum - intInput[index - 3] + intInput[index]
        if (firstSum < secondSum) {
            count++
        }
    }
    println(count)
}