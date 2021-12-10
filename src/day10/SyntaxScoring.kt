package day10

import utils.median
import java.math.BigInteger
import java.util.*

private val closingSymbols = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
)

private val invalidSymbolScores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
)

private val autocompleteSymbolScores = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
)

private fun validate(line: String): Pair<Char?, Stack<Char>> {
    val stack = Stack<Char>()

    for (nextSymbol in line) {
        if (nextSymbol in closingSymbols.keys) {
            stack.push(nextSymbol)
            continue
        }

        val symbolToMatch = stack.pop()
        if (nextSymbol != closingSymbols[symbolToMatch]) {
            return Pair(nextSymbol, stack)
        }
    }

    return Pair(null, stack)
}

private fun autoCompleteScore(stack: Stack<Char>): BigInteger {
    var score = 0.toBigInteger()

    while (stack.isNotEmpty()) {
        score *= 5.toBigInteger()
        val nextAutoCompleteSymbol = closingSymbols[stack.pop()]!!
        score += autocompleteSymbolScores[nextAutoCompleteSymbol]!!.toBigInteger()
    }

    return score
}

fun solvePuzzle1(input: List<String>) {
    val result = input.mapNotNull { validate(it).first }
            .mapNotNull { invalidSymbolScores[it] }
            .sum()

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val incompleteLineScores = input.map { validate(it) }
            .filter { it.first == null }
            .map { autoCompleteScore(it.second) }

    val result = incompleteLineScores.median()

    println(result)
}
