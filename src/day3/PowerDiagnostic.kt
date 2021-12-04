package day3

class PowerDiagnostic(private val rawInput: List<String>) {
    private var bitFrequencies = mutableListOf<List<Int>>()

    init {
        readPowerDiagnostic(rawInput)
    }

    private fun readPowerDiagnostic(input: List<String>) {
        for (position in input[0].indices) {
            val countOfZeroes = input.count { it[position] == '0' }
            val countOfOnes = input.size - countOfZeroes
            bitFrequencies.add(listOf(countOfZeroes, countOfOnes))
        }
    }

    fun getGammaBits(): String {
        var gammaBits = ""

        for (bitFrequency in bitFrequencies) {
            gammaBits += if (bitFrequency[0] > bitFrequency[1]) {
                "0"
            } else {
                "1"
            }
        }

        return gammaBits
    }

    fun getEpsilonBits(): String {
        var epsilonBits = ""

        for (bitFrequency in bitFrequencies) {
            epsilonBits += if (bitFrequency[0] < bitFrequency[1]) {
                "0"
            } else {
                "1"
            }
        }

        return epsilonBits
    }

    fun getOxygenGeneratorBits(): String {
        var possibleValues = rawInput

        var index = 0
        while (possibleValues.size > 1) {
            val partitionedValues = possibleValues.partition { it[index] == '0' }

            possibleValues = if (partitionedValues.first.size > partitionedValues.second.size) {
                partitionedValues.first
            } else {
                partitionedValues.second
            }
            index++
        }
        return possibleValues.single()
    }

    fun getCo2ScrubberBits(): String {
        var possibleValues = rawInput

        var index = 0
        while (possibleValues.size > 1) {
            val partitionedValues = possibleValues.partition { it[index] == '0' }

            possibleValues = if (partitionedValues.first.size <= partitionedValues.second.size) {
                partitionedValues.first
            } else {
                partitionedValues.second
            }
            index++
        }
        return possibleValues.single()
    }
}


fun solvePuzzle1(input: List<String>) {
    val powerDiagnostic = PowerDiagnostic(input)

    val gammaRate = powerDiagnostic.getGammaBits().toInt(2)
    val epsilonRate = powerDiagnostic.getEpsilonBits().toInt(2)

    val result = gammaRate * epsilonRate

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val powerDiagnostic = PowerDiagnostic(input)

    val oxygenGeneratorRating = powerDiagnostic.getOxygenGeneratorBits().toInt(2)
    val co2ScrubberRating = powerDiagnostic.getCo2ScrubberBits().toInt(2)

    val result = oxygenGeneratorRating * co2ScrubberRating

    println(result)
}
