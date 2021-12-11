package day11

class Octopus(var energylevel: Int) {
    val neighbours = mutableListOf<Octopus>()
    private var flashed = false

    fun incrementEnergy() {
        energylevel++
        maybeFlash()
    }

    private fun maybeFlash() {
        if (energylevel > 9 && !flashed) {
            flashed = true
            neighbours.forEach { it.incrementEnergy() }
        }
    }

    fun nextStep(): Boolean {
        if (flashed) {
            energylevel = 0
            flashed = false
            return true
        }
        return false
    }
}

class OctopusOracle(rawInput: List<List<Int>>) {
    private val octopodes = mutableMapOf<Int, MutableMap<Int, Octopus>>()

    init {
        rawInput.forEachIndexed { rowIndex, row ->
            octopodes[rowIndex] = mutableMapOf()
            val currentRow = octopodes[rowIndex]!!
            val rowAbove = octopodes[rowIndex - 1]

            row.forEachIndexed { colIndex, value ->
                val cellAbove = rowAbove?.get(colIndex)
                val cellAboveLeft = rowAbove?.get(colIndex - 1)
                val cellAboveRight = rowAbove?.get(colIndex + 1)
                val cellLeft = currentRow[colIndex - 1]

                val currentCell = Octopus(value)
                currentRow[colIndex] = currentCell

                if (cellAbove != null) {
                    currentCell.neighbours.add(cellAbove)
                    cellAbove.neighbours.add(currentCell)
                }
                if (cellAboveLeft != null) {
                    currentCell.neighbours.add(cellAboveLeft)
                    cellAboveLeft.neighbours.add(currentCell)
                }
                if (cellAboveRight != null) {
                    currentCell.neighbours.add(cellAboveRight)
                    cellAboveRight.neighbours.add(currentCell)
                }
                if (cellLeft != null) {
                    currentCell.neighbours.add(cellLeft)
                    cellLeft.neighbours.add(currentCell)
                }
            }
        }
    }

    fun simulateSteps(numberOfSteps: Int): Int {
        var flashes = 0

        for (step in 0 until numberOfSteps) {
            for (row in octopodes.values) {
                for (octopus in row.values) {
                    octopus.incrementEnergy()
                }
            }
            flashes += octopodes.values.sumBy { row ->
                row.values.count { it.nextStep() }
            }
        }

        return flashes
    }

    fun simulateUntilSynchronization(): Int {
        var step = 0

        while (true) {
            step++
            for (row in octopodes.values) {
                for (octopus in row.values) {
                    octopus.incrementEnergy()
                }
            }
            // Can't use List.all here because it uses short-circuit evaluation and we need every octopus to nextStep
            var allFlashed = true
            for (row in octopodes.values) {
                for (octopus in row.values) {
                    if (!octopus.nextStep()) allFlashed = false
                }
            }
            if (allFlashed) {
                return step
            }
        }
    }
}


fun solvePuzzle1(input: List<String>) {
    val intInput = input.map { line ->
        line.toCharArray().toList().map { it.toString().toInt() }
    }

    val octopusOracle = OctopusOracle(intInput)

    val result = octopusOracle.simulateSteps(100)

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val intInput = input.map { line ->
        line.toCharArray().toList().map { it.toString().toInt() }
    }

    val octopusOracle = OctopusOracle(intInput)

    val result = octopusOracle.simulateUntilSynchronization()

    println(result)
}
