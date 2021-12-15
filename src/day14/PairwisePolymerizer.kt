package day14

import java.math.BigInteger

class PairwisePolymerizer(polymerTemplateString: String, insertionRulesStrings: List<String>) {
    private val insertionRules: Map<Pair<Char, Char>, List<Pair<Char, Char>>>

    private var elementPairFrequencies = mapOf<Pair<Char, Char>, BigInteger>()
    private val elementFrequencies = mutableMapOf<Char, BigInteger>()

    fun elementFrequencies(): Map<Char, BigInteger> {
        return elementFrequencies
    }

    init {
        elementFrequencies[polymerTemplateString[0]] = 1.toBigInteger()

        var previousElement = polymerTemplateString[0]
        val pairFrequencies = mutableMapOf<Pair<Char, Char>, BigInteger>()
        for (polymerChar in polymerTemplateString.substring(1)) {
            elementFrequencies.getOrPut(polymerChar) { 0.toBigInteger() }
            elementFrequencies[polymerChar] = elementFrequencies[polymerChar]!! + 1.toBigInteger()

            val elementPair = Pair(previousElement, polymerChar)
            pairFrequencies.getOrPut(elementPair) { 0.toBigInteger() }
            pairFrequencies[elementPair] = pairFrequencies.getValue(elementPair) + 1.toBigInteger()

            previousElement = polymerChar
        }

        elementPairFrequencies = pairFrequencies

        insertionRules = mutableMapOf()
        for (ruleString in insertionRulesStrings) {
            val (initialElements, insertedElement) = ruleString.split(" -> ")
            val oldPair = Pair(initialElements[0], initialElements[1])
            val newPairs = listOf(
                    Pair(initialElements[0], insertedElement[0]),
                    Pair(insertedElement[0], initialElements[1]),
            )
            insertionRules[oldPair] = newPairs
        }
    }

    private fun executeStep() {
        val newPairFrequencies = mutableMapOf<Pair<Char, Char>, BigInteger>()

        for (elementPairFrequency in elementPairFrequencies) {
            val pairsToInsert = insertionRules.getValue(elementPairFrequency.key)
            for (pairToInsert in pairsToInsert) {
                val currentPairFrequency = newPairFrequencies.getOrPut(pairToInsert) { 0.toBigInteger() }
                newPairFrequencies[pairToInsert] = currentPairFrequency + elementPairFrequency.value
            }
            val newElement = pairsToInsert.first().second
            val newElementCurrentFrequency = elementFrequencies.getOrPut(newElement) { 0.toBigInteger() }
            elementFrequencies[newElement] = newElementCurrentFrequency + elementPairFrequency.value
        }

        elementPairFrequencies = newPairFrequencies
    }

    fun executeSteps(numberOfSteps: Int) {
        for (i in 0 until numberOfSteps) {
            executeStep()
        }
    }
}

fun solvePuzzle1(input: List<String>) {
    val partitionedInput = input.filter { it.isNotEmpty() }.partition { it.contains(" -> ") }

    val insertionRules = partitionedInput.first

    val polymerTemplate = partitionedInput.second.single()

    val polymerizer = PairwisePolymerizer(polymerTemplate, insertionRules)

    polymerizer.executeSteps(10)

    val frequencies = polymerizer.elementFrequencies().values

    val result = frequencies.max()!! - frequencies.min()!!

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val partitionedInput = input.filter { it.isNotEmpty() }.partition { it.contains(" -> ") }

    val insertionRules = partitionedInput.first

    val polymerTemplate = partitionedInput.second.single()

    val polymerizer = PairwisePolymerizer(polymerTemplate, insertionRules)

    polymerizer.executeSteps(40)

    val frequencies = polymerizer.elementFrequencies().values

    val result = frequencies.max()!! - frequencies.min()!!

    println(result)
}
