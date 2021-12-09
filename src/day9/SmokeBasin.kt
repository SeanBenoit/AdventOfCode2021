package day9

class SmokeBasin(rawInput: List<List<Int>>) {
    class Cell(val row: Int, val col: Int, val height: Int) {
        val neighbours = mutableListOf<Cell>()

        fun isLowPoint(): Boolean {
            for (neighbour in neighbours) {
                if (neighbour.height <= height) return false
            }
            return true
        }

        fun riskLevel(): Int = height + 1
    }

    private val cells = mutableMapOf<Int, MutableMap<Int, Cell>>()

    init {
        rawInput.forEachIndexed { rowIndex, row ->
            cells[rowIndex] = mutableMapOf()
            val currentRow = cells[rowIndex]!!
            val rowAbove = cells[rowIndex - 1]

            row.forEachIndexed { colIndex, value ->
                val cellAbove = rowAbove?.get(colIndex)
                val cellLeft = currentRow[colIndex - 1]

                val currentCell = Cell(rowIndex, colIndex, value)
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

    private fun findLowPoints(): List<Cell> {
        return cells.values.flatMap { row ->
            row.values.filter { it.isLowPoint() }
        }
    }

    fun riskLevel(): Int {
        return findLowPoints().sumBy { it.height + 1 }
    }

    private fun measureBasin(cell: Cell): Int {
        var size = 0
        val cellsToCheck = mutableListOf(cell)
        val cellsVisited = mutableMapOf<Int, MutableList<Int>>()

        while (cellsToCheck.isNotEmpty()) {
            val nextCell = cellsToCheck.removeAt(0)
            if (nextCell.height == 9) continue
            if (cellsVisited[nextCell.row]?.contains(nextCell.col) == true) continue

            size++
            cellsVisited.getOrPut(nextCell.row) { mutableListOf() }
                    .add(nextCell.col)
            cellsToCheck.addAll(nextCell.neighbours)
        }

        return size
    }

    fun findBasinSizes(): List<Int> {
        return findLowPoints().map { measureBasin(it) }
    }
}

fun solvePuzzle1(input: List<String>) {
    val intInput = input.map { line ->
        line.toCharArray().toList().map { it.toString().toInt() }
    }

    val result = SmokeBasin(intInput).riskLevel()

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val intInput = input.map { line ->
        line.toCharArray().toList().map { it.toString().toInt() }
    }

    val basinSizesSorted = SmokeBasin(intInput).findBasinSizes().sorted().reversed()

    val result = basinSizesSorted[0] * basinSizesSorted[1] * basinSizesSorted[2]

    println(result)
}
