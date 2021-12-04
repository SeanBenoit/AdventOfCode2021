package day4

class BingoSheet(rawRows: List<String>) {
    class BingoNumber(val value: Int, var marked: Boolean = false)

    private val grid = mutableListOf<List<BingoNumber>>()

    init {
        for (row in rawRows) {
            val bingoRow = row.split(" ")
                    .filter { it.isNotEmpty() }
                    .map { BingoNumber(it.toInt()) }
            grid.add(bingoRow)
        }
    }

    fun playNumber(number: Int) {
        for (row in grid) {
            for (bingoNumber in row) {
                if (bingoNumber.value == number) {
                    bingoNumber.marked = true
                }
            }
        }
    }

    fun getUnmarkedNumbers(): List<Int> {
        return grid.flatMap { row ->
            row.filterNot { it.marked }
                    .map { it.value }
        }
    }

    fun checkForWin(): Boolean {
        return checkForWinningRow() || checkForWinningColumn()
    }

    private fun checkForWinningRow(): Boolean {
        for (row in grid) {
            if (row.all { it.marked }) return true
        }
        return false
    }

    private fun checkForWinningColumn(): Boolean {
        for (column in grid[0].indices) {
            if (grid.all { it[column].marked }) return true
        }
        return false
    }
}

class BingoOptimizer(private val sheets: List<BingoSheet>, private val numbersToPlay: List<Int>) {
    private fun findFirstWin(): Pair<BingoSheet, Int> {
        for (numberToPlay in numbersToPlay) {
            for (sheet in sheets) {
                sheet.playNumber(numberToPlay)
                if (sheet.checkForWin()) return Pair(sheet, numberToPlay)
            }
        }

        throw Exception("no sheet won after all numbers were called")
    }

    private fun findLastWin(): Pair<BingoSheet, Int> {
        lateinit var lastWinner: BingoSheet
        for (numberToPlay in numbersToPlay) {
            for (sheet in sheets) {
                val sheetWasNotWinning = !sheet.checkForWin()
                sheet.playNumber(numberToPlay)
                if (sheetWasNotWinning && sheet.checkForWin()) lastWinner = sheet
            }
            if (sheets.all { it.checkForWin() }) return Pair(lastWinner, numberToPlay)
        }

        throw Exception("no sheet won after all numbers were called")
    }

    fun findOptimalScore(): Int {
        val (sheet, lastNumberCalled) = findFirstWin()

        val sheetUnmarkedTotal = sheet.getUnmarkedNumbers().sum()

        return sheetUnmarkedTotal * lastNumberCalled
    }

    fun findLeastOptimalScore(): Int {
        val (sheet, lastNumberCalled) = findLastWin()

        val sheetUnmarkedTotal = sheet.getUnmarkedNumbers().sum()

        return sheetUnmarkedTotal * lastNumberCalled
    }
}

fun parseBingoSheets(input: List<String>): List<BingoSheet> {
    val sheets = mutableListOf<BingoSheet>()

    val rawNextSheet = mutableListOf<String>()
    for (row in input) {
        if (row.isEmpty()) {
            sheets.add(BingoSheet(rawNextSheet))
            rawNextSheet.clear()
            continue
        }
        rawNextSheet.add(row)
    }

    return sheets
}


fun solvePuzzle1(input: List<String>) {
    val numbersToPlay = input[0].split(",")
            .map { it.toInt() }

    val sheets = parseBingoSheets(input.drop(2))

    val optimizer = BingoOptimizer(sheets, numbersToPlay)

    val result = optimizer.findOptimalScore()

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val numbersToPlay = input[0].split(",")
            .map { it.toInt() }

    val sheets = parseBingoSheets(input.drop(2))

    val optimizer = BingoOptimizer(sheets, numbersToPlay)

    val result = optimizer.findLeastOptimalScore()

    println(result)
}
