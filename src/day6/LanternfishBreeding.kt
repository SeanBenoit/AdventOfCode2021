package day6

import java.math.BigInteger

class LanternfishBreeding(
        initialFish: List<Int>,
        private val breedingCycle: Int,
        private val maturationTime: Int
) {
    private val school = mutableListOf<BigInteger>()

    init {
        for (i in 0 until (breedingCycle + maturationTime)) {
            school.add(0.toBigInteger())
        }

        for (fish in initialFish) {
            school[fish]++
        }
    }

    fun advanceTime(days: Int) {
        for (i in 0 until days) {
            val reproducingFish = school[0]

            for (index in 1 until school.size) {
                school[index - 1] = school[index]
            }

            school[breedingCycle - 1] += reproducingFish
            school[breedingCycle + maturationTime - 1] = reproducingFish
        }
    }

    fun countFish(): BigInteger {
        var total = 0.toBigInteger()
        for (count in school) {
            total += count
        }
        return total
    }
}

fun solvePuzzle1(input: List<String>) {
    val intInput = input[0].split(",").map { it.toInt() }

    val lanternfishBreeding = LanternfishBreeding(intInput, 7, 2)

    lanternfishBreeding.advanceTime(80)

    val result = lanternfishBreeding.countFish()

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val intInput = input[0].split(",").map { it.toInt() }

    val lanternfishBreeding = LanternfishBreeding(intInput, 7, 2)

    lanternfishBreeding.advanceTime(256)

    val result = lanternfishBreeding.countFish()

    println(result)
}
