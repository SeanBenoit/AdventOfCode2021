package day3

class PowerDiagnostic(private val rawInput: List<String>) {
    init {
        println(readPowerDiagnostic(rawInput))
    }

    private fun readPowerDiagnostic(input: List<String>): List<List<Int>> {
        val bitFrequencies = mutableListOf<MutableList<Int>>()

        for (position in input[0].indices) {
            bitFrequencies.add(mutableListOf(0, 0))
        }

        for (line in input) {
            line.forEachIndexed { index, bit ->
                bitFrequencies[index][bit.toString().toInt()]++
            }
        }

        return bitFrequencies
    }

    fun getGammaBits(): String {
        return getGammaBitsHelper(rawInput)
    }

    private fun getGammaBitsHelper(input: List<String>): String {
        var gammaBits = ""
        val bitFrequencies = readPowerDiagnostic(input)

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
        return getEpsilonBitsHelper(rawInput)
    }

    private fun getEpsilonBitsHelper(input: List<String>): String {
        var epsilonBits = ""
        val bitFrequencies = readPowerDiagnostic(input)

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
    println(Integer.toBinaryString(oxygenGeneratorRating).padStart(12, '0'))
    val co2ScrubberRating = powerDiagnostic.getCo2ScrubberBits().toInt(2)
    println(Integer.toBinaryString(co2ScrubberRating))

    val result = oxygenGeneratorRating * co2ScrubberRating

    println(result)
}
