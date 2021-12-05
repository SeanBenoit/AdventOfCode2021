package day5

import kotlin.math.abs
import kotlin.math.sign

class VentMapper(lines: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>, includeDiagonals: Boolean = false) {
    private val ventMap = mutableMapOf<Int, MutableMap<Int, Int>>()

    init {
        for (line in lines) {
            val x1 = line.first.first
            val y1 = line.first.second
            val x2 = line.second.first
            val y2 = line.second.second
            if (isLineDiagonal(x1, y1, x2, y2) && includeDiagonals) {
                addDiagonalLine(x1, y1, x2, y2)
            } else if (!isLineDiagonal(x1, y1, x2, y2)) {
                addLine(x1, y1, x2, y2)
            }
        }
    }

    private fun isLineDiagonal(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return x1 != x2 && y1 != y2
    }

    private fun addLine(x1: Int, y1: Int, x2: Int, y2: Int) {
        val xStart = minOf(x1, x2)
        val xEnd = maxOf(x1, x2)
        val yStart = minOf(y1, y2)
        val yEnd = maxOf(y1, y2)
        for (x in xStart..xEnd) {
            val row = ventMap.getOrPut(x) { mutableMapOf() }
            for (y in yStart..yEnd) {
                val cell = row.getOrPut(y) { 0 }
                row[y] = cell + 1
            }
        }
    }

    private fun addDiagonalLine(x1: Int, y1: Int, x2: Int, y2: Int) {
        val lineLength = abs(x1 - x2)
        val xDirection = (x2 - x1).sign
        val yDirection = (y2 - y1).sign
        for (i in 0..lineLength) {
            val row = ventMap.getOrPut(x1 + i * xDirection) { mutableMapOf() }
            val y = y1 + i * yDirection
            val cell = row.getOrPut(y) { 0 }
            row[y] = cell + 1
        }
    }

    fun countOverlaps(): Int {
        val rowsWithOverlap = ventMap.filterValues { row ->
            row.filterValues { it > 1 }
                    .isNotEmpty()
        }
        return rowsWithOverlap.values.sumBy { row ->
            row.count {
                it.value > 1
            }
        }
    }
}

fun solvePuzzle1(input: List<String>) {
    val inputLines = input.mapNotNull { rawLine ->
        val rawCoords = rawLine.split(" -> ")
        val startCoords = rawCoords[0].split(",")
                .map { it.toInt() }
        val endCoords = rawCoords[1].split(",")
                .map { it.toInt() }
        if (startCoords[0] != endCoords[0] && startCoords[1] != endCoords[1]) {
            null
        } else {
            Pair(
                    Pair(startCoords[0], startCoords[1]),
                    Pair(endCoords[0], endCoords[1])
            )
        }
    }

    val ventMapper = VentMapper(inputLines)

    val result = ventMapper.countOverlaps()

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val inputLines = input.map { rawLine ->
        val rawCoords = rawLine.split(" -> ")
        val startCoords = rawCoords[0].split(",")
                .map { it.toInt() }
        val endCoords = rawCoords[1].split(",")
                .map { it.toInt() }
        Pair(
                Pair(startCoords[0], startCoords[1]),
                Pair(endCoords[0], endCoords[1])
        )
    }

    val ventMapper = VentMapper(inputLines, includeDiagonals = true)

    val result = ventMapper.countOverlaps()

    println(result)
}
