package day18

import java.util.*

class SnailNumber() {
    var left: SnailNumber? = null
    var right: SnailNumber? = null
    var parent: SnailNumber? = null
    private var value: Int? = null

    // Parse a string to a SnailNumber
    constructor(
            rawInput: String,
            parent: SnailNumber? = null,
    ) : this() {
        this.parent = parent
        value = rawInput.toIntOrNull()
        if ('[' in rawInput) {
            // Find left token and the right token
            var leftToken = ""
            var rightToken = rawInput.drop(1).dropLast(1)
            val stack = Stack<Char>()
            while (rightToken.isNotEmpty()) {
                val nextSymbol = rightToken.first()
                rightToken = rightToken.drop(1)

                // We've reached the comma separating the left and right tokens
                if (nextSymbol == ',' && stack.isEmpty()) {
                    break
                }
                leftToken += nextSymbol
                if (nextSymbol == '[') {
                    stack.push(nextSymbol)
                } else if (nextSymbol == ']') {
                    stack.pop()
                }
            }
            left = SnailNumber(leftToken, parent = this)
            right = SnailNumber(rightToken, parent = this)
        }
    }

    // Create a SnailNumber with no children and a specific value
    constructor(value: Int) : this() {
        this.value = value
    }

    // Create a SnailNumber with specific children and no value
    constructor(left: SnailNumber, right: SnailNumber) : this() {
        this.left = left
        this.right = right
        left.parent = this
        right.parent = this
    }

    fun add(other: SnailNumber): SnailNumber {
        // TODO: Make this work by using copies of this and other
        // val result = SnailNumber(this, other)
        val result = SnailNumber("[$this,$other]")

        result.reduce()

        return result
    }

    private fun reduce() {
        while (reduceHelperExplosions() || reduceHelperSplits()) {
            // This will definitely terminate. Don't worry about it.
            // This is fine.
        }
    }

    private fun reduceHelperExplosions(depth: Int = 0): Boolean {
        if (depth >= 4 && value == null) {
            explode()
            return true
        }
        if (left?.reduceHelperExplosions(depth + 1) == true) {
            return true
        }
        if (right?.reduceHelperExplosions(depth + 1) == true) {
            return true
        }
        return false
    }

    private fun reduceHelperSplits(): Boolean {
        if (value != null) {
            if (value!! > 9) {
                split()
                return true
            }
            return false
        }
        return left!!.reduceHelperSplits() || right!!.reduceHelperSplits()
    }

    private fun explode() {
        require(left != null && right != null) {
            "can only explode SnailNumbers that are not regular numbers: ${toString()}"
        }
        require(left?.value != null && right?.value != null) {
            "can only explode SnailNumbers whose children are regular numbers: ${toString()}"
        }

        // Find next value to the left, if it exists
        var nodeToExplore: SnailNumber? = this
        while (nodeToExplore != null) {
            if (nodeToExplore.parent?.right == nodeToExplore) {
                nodeToExplore = nodeToExplore.parent!!.left
                break
            }
            nodeToExplore = nodeToExplore.parent
        }
        while (nodeToExplore != null && nodeToExplore.value == null) {
            nodeToExplore = nodeToExplore.right
        }
        val nextValueToLeft = nodeToExplore

        // Find next value to the right, if it exists
        nodeToExplore = this
        while (nodeToExplore != null) {
            if (nodeToExplore.parent?.left == nodeToExplore) {
                nodeToExplore = nodeToExplore.parent!!.right
                break
            }
            nodeToExplore = nodeToExplore.parent
        }
        while (nodeToExplore != null && nodeToExplore.value == null) {
            nodeToExplore = nodeToExplore.left
        }
        val nextValueToRight = nodeToExplore

        // Add to the adjacent values
        if (nextValueToLeft != null) {
            nextValueToLeft.value = nextValueToLeft.value!! + left!!.value!!
        }
        if (nextValueToRight != null) {
            nextValueToRight.value = nextValueToRight.value!! + right!!.value!!
        }

        // Set this to the regular value 0
        value = 0
        left!!.parent = null
        left = null
        right!!.parent = null
        right = null
    }

    private fun split() {
        require(value != null) { "can only split regular numbers" }

        val newLeftValue = value!! / 2
        val newRightValue = value!! - newLeftValue

        left = SnailNumber(newLeftValue)
        left!!.parent = this
        right = SnailNumber(newRightValue)
        right!!.parent = this

        value = null
    }

    fun magnitude(): Int {
        return value ?: 3 * left!!.magnitude() + 2 * right!!.magnitude()
    }

    override fun toString(): String {
        return if (value != null) {
            value.toString()
        } else {
            "[${left?.toString()},${right?.toString()}]"
        }
    }
}

fun computeLargestMagnitudeOfPair(snailNumbers: List<SnailNumber>): Int {
    var largestMagnitudeSoFar = 0

    for (i in snailNumbers.indices) {
        for (j in snailNumbers.indices) {
            if (i == j) {
                continue
            }
            val sumOfPair = snailNumbers[i].add(snailNumbers[j])
            largestMagnitudeSoFar = maxOf(largestMagnitudeSoFar, sumOfPair.magnitude())
        }
    }

    return largestMagnitudeSoFar
}

fun solvePuzzle1(input: List<String>) {
    val snailNumbers = input.filter { it.isNotEmpty() }
            .map { SnailNumber(it) }

    var sum = snailNumbers.first()
    for (nextNumber in snailNumbers.drop(1)) {
        sum = sum.add(nextNumber)
    }

    val result = sum.magnitude()

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val snailNumbers = input.filter { it.isNotEmpty() }
            .map { SnailNumber(it) }

    val result = computeLargestMagnitudeOfPair(snailNumbers)

    println(result)
}
