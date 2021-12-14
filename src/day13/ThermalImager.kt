package day13

class ThermalImager(rawInput: List<String>) {
    private val image = mutableMapOf<Int, MutableSet<Int>>()
    private val foldInstructions = mutableListOf<String>()

    init {
        val partitionedInput = rawInput.partition { it.startsWith("fold along ") }

        foldInstructions.addAll(partitionedInput.first)

        partitionedInput.second.filter { it.isNotEmpty() }
                .forEach { rawCoord ->
                    val (x, y) = rawCoord.split(",").map { it.toInt() }
                    image.getOrPut(x) { mutableSetOf() }.add(y)
                }
    }

    private fun foldAlongY(y: Int) {
        for (row in image) {
            val newRow = mutableSetOf<Int>()
            for (dot in row.value) {
                if (dot < y) {
                    newRow.add(dot)
                } else {
                    val newY = y - (dot - y)
                    newRow.add(newY)
                }
            }
            image[row.key] = newRow
        }
    }

    private fun foldAlongX(x: Int) {
        val rowsBelowLine = image.filterKeys { it > x }

        for (row in rowsBelowLine) {
            val newX = 2 * x - row.key
            image.getOrPut(newX) { mutableSetOf() }
                    .addAll(row.value)
            image.remove(row.key)
        }
    }

    private fun executeFold(instruction: String) {
        val coord = instruction.split("=")[1].toInt()
        when {
            instruction.contains("x") -> foldAlongX(coord)
            instruction.contains("y") -> foldAlongY(coord)
            else -> throw Exception("invalid fold instruction: $instruction")
        }
    }

    fun dotsAfterFirstFold(): Int {
        executeFold(foldInstructions.first())
        return image.values.sumBy { it.size }
    }

    fun executeAllFolds() {
        for (instruction in foldInstructions) {
            executeFold(instruction)
        }
    }

    fun print() {
        val width = image.keys.max() ?: throw Exception("image has no width")
        val height = image.values.maxBy { it.max() ?: 0 }?.max() ?: throw Exception("image has no height")

        for (y in 0..height) {
            for (x in 0..width) {
                if (image[x]?.contains(y) == true) {
                    print("#")
                } else {
                    print(" ")
                }
            }
            println()
        }
    }
}

fun solvePuzzle1(input: List<String>) {
    val imager = ThermalImager(input)

    val result = imager.dotsAfterFirstFold()

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val imager = ThermalImager(input)

    imager.executeAllFolds()

    imager.print()
}
