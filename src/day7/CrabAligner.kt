package day7

import utils.median
import kotlin.math.abs
import kotlin.math.roundToInt

class CrabAligner(private val crabs: List<Int>) {
    fun minimumFuelRequired(): Int {
        val medianPosition = medianCrabPosition()

        return crabs.sumBy { abs(it - medianPosition) }
    }

    fun minimumQuadraticFuelRequired(): Int {
        val meanPosition = meanCrabPosition()

        // Sometimes rounding to the nearest int is correct, rounding to the farther int is correct.
        // This probably depends on how many values are above/below the mean and by how much, but the problem
        // is small enough that I can afford to check all three possibilities and find the minimum.
        // TODO: Find out how to do this without checking all three possibilities.
        return minOf(
                crabs.sumBy { quadraticFuelCost(it, meanPosition - 1) },
                crabs.sumBy { quadraticFuelCost(it, meanPosition) },
                crabs.sumBy { quadraticFuelCost(it, meanPosition + 1) },
        )
    }

    private fun medianCrabPosition(): Int {
        return crabs.median()
    }

    private fun meanCrabPosition(): Int {
        return crabs.average().roundToInt()
    }

    private fun quadraticFuelCost(start: Int, target: Int): Int {
        val distance = abs(start - target)
        return distance * (distance + 1) / 2
    }
}


fun solvePuzzle1(input: List<String>) {
    val intInput = input[0].split(",").map { it.toInt() }

    val crabAligner = CrabAligner(intInput)

    val result = crabAligner.minimumFuelRequired()

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val intInput = input[0].split(",").map { it.toInt() }

    val crabAligner = CrabAligner(intInput)

    val result = crabAligner.minimumQuadraticFuelRequired()

    println(result)
}
