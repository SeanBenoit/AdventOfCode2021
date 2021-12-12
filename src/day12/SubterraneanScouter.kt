package day12

private const val startLabel = "start"
private const val targetLabel = "end"

class Cave(val label: String) {
    val connectedCaves = mutableSetOf<Cave>()

    fun isSmallCave(): Boolean {
        return label.all { it.isLowerCase() }
    }
}

fun constructCaves(input: List<String>): Map<String, Cave> {
    val caves = mutableMapOf<String, Cave>()

    for (line in input) {
        val (firstCaveLabel, secondCaveLabel) = line.split("-")
        val firstCave = caves.getOrPut(firstCaveLabel) { Cave(firstCaveLabel) }
        val secondCave = caves.getOrPut(secondCaveLabel) { Cave(secondCaveLabel) }

        firstCave.connectedCaves.add(secondCave)
        secondCave.connectedCaves.add(firstCave)
    }

    return caves
}

fun listAllPaths(startCave: Cave): List<List<String>> {
    return listAllPathsFrom(startCave, listOf())!!
}

fun listAllPathsFrom(startCave: Cave, pathPrefix: List<String>): List<List<String>>? {
    if (startCave.isSmallCave() && pathPrefix.contains(startCave.label)) return null

    val paths = mutableListOf<List<String>>()
    val newPathPrefix = pathPrefix + startCave.label

    if (startCave.label == targetLabel) {
        return listOf(newPathPrefix)
    }

    for (cave in startCave.connectedCaves) {
        val newPaths = listAllPathsFrom(cave, newPathPrefix)

        if (newPaths != null) paths.addAll(newPaths)
    }

    return paths
}

fun listAllPathsWithADouble(startCave: Cave): List<List<String>> {
    return listAllPathsWithADoubleFrom(startCave, listOf())!!
}

fun listAllPathsWithADoubleFrom(startCave: Cave, pathPrefix: List<String>): List<List<String>>? {
    if (startCave.label == startLabel && pathPrefix.isNotEmpty()) {
        return null
    }

    val newPathPrefix = pathPrefix + startCave.label
    if (startCave.label == targetLabel) {
        return listOf(newPathPrefix)
    }

    val paths = mutableListOf<List<String>>()

    var listPathEnds: (Cave, List<String>) -> List<List<String>>? = ::listAllPathsWithADoubleFrom
    if (startCave.isSmallCave() && pathPrefix.contains(startCave.label)) listPathEnds = ::listAllPathsFrom

    for (cave in startCave.connectedCaves) {
        val newPaths = listPathEnds(cave, newPathPrefix)

        if (newPaths != null) paths.addAll(newPaths)
    }

    return paths
}

fun solvePuzzle1(input: List<String>) {
    val caves = constructCaves(input)

    val allPaths = listAllPaths(caves[startLabel]!!)

    val result = allPaths.size

    println(result)
}

fun solvePuzzle2(input: List<String>) {
    val caves = constructCaves(input)

    val allPaths = listAllPathsWithADouble(caves[startLabel]!!)

    val result = allPaths.size

    println(result)
}
