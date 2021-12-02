package day2

import day1.countIncreasing
import day1.slidingWindowSums

class Submarine {
    var depth = 0
    var horizontalPosition = 0
    var aim = 0

    fun executeInstruction(instruction: String) {
        val instructionParts = instruction.split(" ")
        val command = instructionParts[0]
        val amount = instructionParts[1].toInt()

        when (command) {
            "forward" -> moveForward(amount)
            "down" -> dive(amount)
            "up" -> climb(amount)
        }
    }

    fun executeInstructionV2(instruction: String) {
        val instructionParts = instruction.split(" ")
        val command = instructionParts[0]
        val amount = instructionParts[1].toInt()

        when (command) {
            "forward" -> moveForwardV2(amount)
            "down" -> aimDown(amount)
            "up" -> aimUp(amount)
        }
    }

    private fun moveForward(distance: Int) {
        horizontalPosition += distance
    }

    private fun moveForwardV2(distance: Int) {
        horizontalPosition += distance
        depth += distance * aim
    }

    private fun dive(distance: Int) {
        depth += distance
    }

    private fun climb(distance: Int) {
        depth -= distance
    }

    private fun aimDown(amount: Int) {
        aim += amount
    }

    private fun aimUp(amount: Int) {
        aim -= amount
    }
}

fun solvePuzzle1(input: List<String>) {
    val submarine = Submarine()

    input.forEach { submarine.executeInstruction(it) }

    val result = submarine.depth * submarine.horizontalPosition

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val submarine = Submarine()

    input.forEach { submarine.executeInstructionV2(it) }

    val result = submarine.depth * submarine.horizontalPosition

    println(result)
}
