fun main() {
    fun List<Long>.getExpandSequencesOfDifferences(): List<List<Long>> {
        val sequences = mutableListOf(this)
        while (!sequences.last().all { it == 0L }) {
            sequences += sequences.last().windowed(2).map { (left, right) -> right - left }
        }
        return sequences
    }

    fun List<Long>.nextElement(): Long {
        return getExpandSequencesOfDifferences()
            .foldRight(0) { currentList, acc -> acc + currentList.last() }
    }

    fun List<Long>.previousElement(): Long {
        return getExpandSequencesOfDifferences()
            .foldRight(0) { currentList, acc -> currentList.first() - acc }
    }

    fun part1(input: List<String>): Long {
        return input.map { line -> line.split(' ').map { it.toLong() } }
            .sumOf { it.nextElement() }
    }

    fun part2(input: List<String>): Long {
        return input.map { line -> line.split(' ').map { it.toLong() } }
            .sumOf { it.previousElement() }
    }

    val part1TestInput = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent().lines()

    check(part1(part1TestInput).also { println("Part 1 Test 1: $it") } == 114L)
    check(part2(part1TestInput).also { println("Part 2 Test: $it") } == 2L)

    val input = readInput("inputDay9")
    part1(input).also { println("Part 1: $it") }
    part2(input).also { println("Part 2: $it") }
}
