package day17

import kotlin.math.floor
import kotlin.math.sign
import kotlin.math.sqrt

fun getTargetRanges(input: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val regex = "target area: x=(-?\\d+)\\.\\.(-?\\d+), y=(-?\\d+)\\.\\.(-?\\d+)".toRegex()
    val (xMin, xMax, yMin, yMax) = regex.find(input)!!.destructured
    return Pair(
            Pair(xMin.toInt(), xMax.toInt()),
            Pair(yMin.toInt(), yMax.toInt()),
    )
}

fun findYeetiestYVelocity(yMin: Int, yMax: Int): Int {
    // There's no drag in the y direction so y position over time is a perfect parabola.
    return if (yMin <= 0) {
        // This means that for any positive y velocity, it will hit yPos = 0 at some point with y velocity equal to the
        // negative initial y velocity.
        // Thus the step after that will take us to yPos = -1 * (initial y velocity + 1).
        // Solving for the lowest possible final y position gives the fastest initial y velocity that hits the target and
        // thus the greatest maximum height.
        -yMin - 1
    } else {
        // The target is above the starting position. Symmetry means that if we aim to hit the top of the target in
        // the first step, we will hit the target on the way down.
        yMax
    }
}

fun findMaximumYPos(initialVelocity: Int): Int {
    return initialVelocity * (initialVelocity + 1) / 2
}

fun findMaxXVelocity(xMax: Int): Int {
    return xMax
}

fun findMinXVelocity(xMin: Int): Int {
    val weirdPart = sqrt((1 - 4 * 1 * -2 * (xMin - 1)).toDouble()) / 2

    val exactMinX = 0.5 + weirdPart

    return floor(exactMinX).toInt()
}

fun findLeastYeetyYVelocity(yMin: Int): Int {
    return if (yMin <= 0) {
        yMin
    } else {
        findMinXVelocity(yMin)
    }
}

fun willHitTarget(
        initialXVelocity: Int,
        initialYVelocity: Int,
        xMin: Int,
        xMax: Int,
        yMin: Int,
        yMax: Int,
): Boolean {
    var xVelocity = initialXVelocity
    var yVelocity = initialYVelocity

    var xPos = 0
    var yPos = 0

    while (xPos <= xMax && (yPos >= yMin || yVelocity >= 0)) {
        xPos += xVelocity
        yPos += yVelocity

        if (xPos in xMin..xMax && yPos in yMin..yMax) return true

        xVelocity -= xVelocity.sign
        yVelocity -= 1
    }

    return false
}

fun countValidInitialVelocities(xMin: Int, xMax: Int, yMin: Int, yMax: Int): Int {
    var count = 0

    val minXVelocity = findMinXVelocity(xMin)
    val maxXVelocity = findMaxXVelocity(xMax)
    val minYVelocity = findLeastYeetyYVelocity(yMin)
    val maxYVelocity = findYeetiestYVelocity(yMin, yMax)

    for (possibleXVel in minXVelocity..maxXVelocity) {
        for (possibleYVel in minYVelocity..maxYVelocity) {
            if (
                    willHitTarget(
                            possibleXVel,
                            possibleYVel,
                            xMin,
                            xMax,
                            yMin,
                            yMax,
                    )
            ) {
                count++
            }
        }
    }

    return count
}

fun solvePuzzle1(input: List<String>) {
    val targetRanges = getTargetRanges(input.first())

    val initialYvelocity = findYeetiestYVelocity(targetRanges.second.first, targetRanges.second.second)

    val result = findMaximumYPos(initialYvelocity)

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val targetRanges = getTargetRanges(input.first())

    val result = countValidInitialVelocities(
            targetRanges.first.first,
            targetRanges.first.second,
            targetRanges.second.first,
            targetRanges.second.second,
    )

    println(result)
}
