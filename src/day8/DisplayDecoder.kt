package day8

private val uniqueDigitLengths = mapOf(
        1 to 2,
        4 to 4,
        7 to 3,
        8 to 7,
)
private val uniqueDigits = uniqueDigitLengths.keys

fun countUniqueDigits(outputValues: List<List<String>>): Int {
    return outputValues.sumBy {
        it.count { value -> value.length in uniqueDigitLengths }
    }
}

fun decode(input: List<List<String>>): Int {
    val segmentPatterns = determineMapping(input.first())

    var value = 0
    for (digit in input.last()) {
        value *= 10
        value += segmentPatterns[digit.alphabetized()]!!
    }

    return value
}

private fun determineMapping(scrambledPatterns: List<String>): Map<String, Int> {
    val scrambledSegmentsInDigit = mutableMapOf<Int, String>()

    for (pattern in scrambledPatterns) {
        for (uniqueDigit in uniqueDigits) {
            if (pattern.length == uniqueDigitLengths[uniqueDigit]) {
                scrambledSegmentsInDigit[uniqueDigit] = pattern
            }
        }
    }

    val fiveSegmentPatterns = scrambledPatterns.filter { it.length == 5 }
    val sixSegmentPatterns = scrambledPatterns.filter { it.length == 6 }

    scrambledSegmentsInDigit[3] = fiveSegmentPatterns.single { it.containsAllOf(scrambledSegmentsInDigit[1]!!) }

    scrambledSegmentsInDigit[6] = sixSegmentPatterns.single { !it.containsAllOf(scrambledSegmentsInDigit[1]!!) }

    scrambledSegmentsInDigit[9] = sixSegmentPatterns.single { it.containsAllOf(scrambledSegmentsInDigit[3]!!) }

    val possibleZeroPatterns = sixSegmentPatterns.toMutableList()
    possibleZeroPatterns.remove(scrambledSegmentsInDigit[6]!!)
    possibleZeroPatterns.remove(scrambledSegmentsInDigit[9]!!)
    scrambledSegmentsInDigit[0] = possibleZeroPatterns.single()

    val mapsToC = scrambledSegmentsInDigit[1]!!.filterNot { it in scrambledSegmentsInDigit[6]!! }

    scrambledSegmentsInDigit[5] = fiveSegmentPatterns.single { !it.contains(mapsToC) }

    scrambledSegmentsInDigit[2] = (
            fiveSegmentPatterns - scrambledSegmentsInDigit[3]!! - scrambledSegmentsInDigit[5]!!
            ).single()

    return scrambledSegmentsInDigit.entries.associate { (digit, pattern) ->
        pattern.alphabetized() to digit
    }
}

fun String.alphabetized(): String {
    return this.toCharArray().sorted().joinToString(separator = "")
}

fun String.containsAllOf(other: String): Boolean {
    for (char in other) {
        if (!this.contains(char)) return false
    }
    return true
}

fun solvePuzzle1(input: List<String>) {
    val outputValues = input.map {
        it.split(" | ")[1]
                .split(" ")
    }

    val result = countUniqueDigits(outputValues)

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val splitInput = input.map { line ->
        line.split(" | ")
                .map { it.split(" ") }
    }

    val result = splitInput.sumBy { decode(it) }

    println(result)
}
