package day15

import java.util.*
import kotlin.Int.Companion.MAX_VALUE
import kotlin.math.abs

typealias Grid = List<List<Int>>

class ChitonPather(rawGrid: Grid) {
    private val grid: Map<Int, MutableMap<Int, GridCell>>

    init {
        grid = mutableMapOf()

        rawGrid.forEachIndexed { rowIndex: Int, rawRow: List<Int> ->
            val currentRow = grid.getOrPut(rowIndex) { mutableMapOf() }
            val rowAbove = grid[rowIndex - 1]

            rawRow.forEachIndexed { colIndex, risk ->
                val cellAbove = rowAbove?.get(colIndex)
                val cellLeft = currentRow[colIndex - 1]

                val location = Pair(rowIndex, colIndex)
                val currentCell = GridCell(risk, location)
                currentRow[colIndex] = currentCell

                if (cellAbove != null) {
                    currentCell.neighbours.add(cellAbove)
                    cellAbove.neighbours.add(currentCell)
                }
                if (cellLeft != null) {
                    currentCell.neighbours.add(cellLeft)
                    cellLeft.neighbours.add(currentCell)
                }
            }
        }
    }

    data class GridCell(val risk: Int, val location: Pair<Int, Int>) {
        val neighbours = mutableListOf<GridCell>()

        var bestEstimatedRisk: Int = MAX_VALUE
        var bestRisk: Int = MAX_VALUE

        fun manhattanDistanceTo(b: Pair<Int, Int>): Int {
            return abs(location.first - b.first) + abs(location.second - b.second)
        }
    }

    fun AStar(start: Pair<Int, Int>, goal: Pair<Int, Int>): Int {
        val startingCell = grid.getValue(start.first).getValue(start.second)
        startingCell.bestRisk = 0
        startingCell.bestEstimatedRisk = startingCell.manhattanDistanceTo(goal)

        val cellsToCheck = PriorityQueue<GridCell>(
                compareBy { it.bestEstimatedRisk }
        )
        cellsToCheck.add(startingCell)

        while (cellsToCheck.isNotEmpty()) {
            val nextCell = cellsToCheck.poll()!!

            val riskSoFar = nextCell.bestRisk

            if (nextCell.location == goal) {
                return riskSoFar
            }

            for (neighbour in nextCell.neighbours) {
                val neighbourBestRisk = riskSoFar + neighbour.risk

                if (neighbourBestRisk < neighbour.bestRisk) {
                    neighbour.bestRisk = neighbourBestRisk
                    neighbour.bestEstimatedRisk = neighbourBestRisk + neighbour.manhattanDistanceTo(goal)
                    if (neighbour !in cellsToCheck) cellsToCheck.add(neighbour)
                }
            }
        }

        throw Exception("failed to find a path somehow?")
    }
}

fun makeInputWider(input: Grid, times: Int): Grid {
    val newInput = mutableListOf<List<Int>>()

    for (row in input) {
        val newRow = mutableListOf<Int>()
        newRow.addAll(row)
        var modifiedRow = row
        for (i in 1 until times) {
            modifiedRow = modifiedRow.map { (it % 9) + 1 }
            newRow.addAll(modifiedRow)
        }
        newInput.add(newRow)
    }

    return newInput
}

fun makeInputTaller(input: Grid, times: Int): Grid {
    val newInput = input.toMutableList()

    var previousIteration = input
    for (i in 1 until times) {
        val nextIteration = previousIteration.map { row ->
            row.map { (it % 9) + 1 }
        }
        newInput.addAll(nextIteration)
        previousIteration = nextIteration
    }

    return newInput
}

fun solvePuzzle1(input: List<String>) {
    val intInput = input.map { row ->
        row.map { it.toString().toInt() }
    }

    val maxRow = intInput.size - 1
    val maxCol = intInput.first().size - 1

    val start = Pair(0, 0)
    val goal = Pair(maxRow, maxCol)

    val chitonPather = ChitonPather(intInput)

    val result = chitonPather.AStar(start, goal)

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val intInput = input.map { row ->
        row.map { it.toString().toInt() }
    }

    val wideInput = makeInputWider(intInput, 5)
    val bigInput = makeInputTaller(wideInput, 5)

    val maxRow = bigInput.size - 1
    val maxCol = bigInput.first().size - 1

    val start = Pair(0, 0)
    val goal = Pair(maxRow, maxCol)

    val chitonPather = ChitonPather(bigInput)

    val result = chitonPather.AStar(start, goal)

    println(result)
}
