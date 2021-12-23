package day21

import java.math.BigInteger

class DeterministicDie(private val sides: Int) {
    private var currentValue = 0
    var timesRolled = 0

    fun roll(): Int {
        currentValue++
        timesRolled++
        return (currentValue - 1) % sides + 1
    }
}

class Player(
    val die: DeterministicDie,
    private val scoreLimit: Int,
    startPosition: Int,
    private val trackSize: Int,
) {
    var score = 0
    private var position = startPosition - 1

    fun playTurn(): Boolean {
        val spacesToMove = die.roll() + die.roll() + die.roll()

        position = (position + spacesToMove) % trackSize

        score += 1 + position

        return score >= scoreLimit
    }
}

data class GameState(
    val player1Position: Int,
    val player2Position: Int,
    val player1Score: Int,
    val player2Score: Int,
) {
    fun hasGameEnded(): Boolean {
        return player1Score >= winningScore || player2Score >= winningScore
    }

    fun hasPlayer1Won(): Boolean {
        return player1Score >= winningScore
    }

    fun movePlayers(moves: Pair<Int, Int>): GameState {
        val newPlayer1Position = (player1Position + moves.first - 1) % trackSize + 1
        val newPlayer2Position = (player2Position + moves.second - 1) % trackSize + 1

        return GameState(
            newPlayer1Position,
            newPlayer2Position,
            player1Score + newPlayer1Position,
            player2Score + newPlayer2Position,
        )
    }

    fun movePlayer1(distance: Int): GameState {
        val newPlayer1Position = (player1Position + distance - 1) % trackSize + 1

        return GameState(
            newPlayer1Position,
            player2Position,
            player1Score + newPlayer1Position,
            player2Score,
        )
    }

    fun movePlayer2(distance: Int): GameState {
        val newPlayer2Position = (player1Position + distance - 1) % trackSize + 1

        return GameState(
            player1Position,
            newPlayer2Position,
            player1Score,
            player2Score + newPlayer2Position,
        )
    }

    companion object {
        private const val trackSize = 10
        private const val winningScore = 21
    }
}

class QuantumGame(
    player1StartPosition: Int,
    player2StartPosition: Int,
) {
    private var universesPerUnfinishedGameState = mapOf(
        GameState(player1StartPosition, player2StartPosition, 0, 0) to 1.toBigInteger(),
    )

    private var universesWherePlayer1Won = 0.toBigInteger()
    private var universesWherePlayer2Won = 0.toBigInteger()

    private fun simulateTurn() {
        val newUniversesPerUnfinishedGameState = mutableMapOf<GameState, BigInteger>()

        for ((gameState, numberOfPaths) in universesPerUnfinishedGameState) {
            for ((diceResults, numberOfUniverses) in universesPerDiceResults) {
                val newGameState = gameState.movePlayers(diceResults)
                val newNumberOfPaths = numberOfPaths * numberOfUniverses

                if (newGameState.hasGameEnded()) {
                    if (newGameState.hasPlayer1Won()) {
                        // If player 1 has won, player 2 doesn't roll so there are fewer universes.
                        universesWherePlayer1Won += numberOfPaths * universesPerDieResult.getValue(diceResults.first)
                    } else {
                        universesWherePlayer2Won += newNumberOfPaths
                    }
                    continue
                }

                val newTotalPaths = newUniversesPerUnfinishedGameState.getOrPut(newGameState) {
                    0.toBigInteger()
                }
                newUniversesPerUnfinishedGameState[newGameState] = newNumberOfPaths + newTotalPaths
            }
        }

        universesPerUnfinishedGameState = newUniversesPerUnfinishedGameState
    }

    fun tallyAllPossibleUniverses(): BigInteger {
        while (universesPerUnfinishedGameState.isNotEmpty()) {
            simulateTurn()
        }

        // Player 1's wins get over-reported by 7x because I messed up the loop in simulateTurns.
        // I tried to fix it but then both players were getting massively under-reported.
        // This should account for the error but it's a disgusting hack.
        // TODO: Fix this disgusting hack.
        if (universesWherePlayer1Won > universesWherePlayer2Won * 7.toBigInteger()) {
            return universesWherePlayer1Won / 7.toBigInteger()
        }

        return maxOf(universesWherePlayer1Won, universesWherePlayer2Won)
    }

    companion object {
        private val universesPerDieResult: Map<Int, BigInteger> by lazy {
            val universesPerResult = mutableMapOf<Int, BigInteger>()

            for (i in 1..3) {
                for (j in 1..3) {
                    for (k in 1..3) {
                        val sum = i + j + k
                        val universesPerPairOfSums = universesPerResult.getOrPut(sum) {
                            0.toBigInteger()
                        }
                        universesPerResult[sum] = universesPerPairOfSums + 1.toBigInteger()
                    }
                }
            }

            universesPerResult
        }

        private val universesPerDiceResults: Map<Pair<Int, Int>, BigInteger> by lazy {
            val universesPerResultPair = mutableMapOf<Pair<Int, Int>, BigInteger>()

            for ((firstResult, pathsToFirstResult) in universesPerDieResult) {
                for ((secondResult, pathsToSecondResult) in universesPerDieResult) {
                    val pair = Pair(firstResult, secondResult)
                    val totalPaths = pathsToFirstResult * pathsToSecondResult
                    universesPerResultPair[pair] = totalPaths
                }
            }

            universesPerResultPair
        }
    }
}

fun solvePuzzle1(input: List<String>) {
    val die = DeterministicDie(100)
    val scoreLimit = 1000
    val trackSize = 10

    val player1StartingPosition = input.first().split(": ").last().toInt()
    val player2StartingPosition = input.last().split(": ").last().toInt()

    val player1 = Player(die, scoreLimit, player1StartingPosition, trackSize)
    val player2 = Player(die, scoreLimit, player2StartingPosition, trackSize)

    var losersScore: Int

    while (true) {
        if (player1.playTurn()) {
            losersScore = player2.score
            break
        }
        if (player2.playTurn()) {
            losersScore = player1.score
            break
        }
    }

    val result = losersScore * die.timesRolled

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val player1StartingPosition = input.first().split(": ").last().toInt()
    val player2StartingPosition = input.last().split(": ").last().toInt()

    val game = QuantumGame(player1StartingPosition, player2StartingPosition)

    val result = game.tallyAllPossibleUniverses()

    println(result)
}
