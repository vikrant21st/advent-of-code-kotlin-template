fun main() {
    fun String.matchCount(): Int =
        drop(indexOf(':') + 1)
            .splitNTrim('|')
            .map { it.split("\\s+".toRegex()) }
            .let { (winningNumbers, numbersYouHave) ->
                winningNumbers.count { it in numbersYouHave }
            }

    fun part1(cards: List<String>): Int {
        fun String.points(): Int =
            when (matchCount()) {
                0 -> 0
                else -> (2..matchCount()).fold(1) { acc, _ -> acc * 2 }
            }

        return cards.sumOf { card -> card.points() }
    }

    fun part2(cards: List<String>): Int {
        val copiesCount = Array(cards.size) { 1 }
        for ((cardNo, card) in cards.withIndex()) {
            for (it in (cardNo + 1)..(cardNo + card.matchCount())) {
                copiesCount[it] += copiesCount[cardNo]
            }
        }
        return copiesCount.sum()
    }

    val part1TestInput = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
        """.trimIndent().lines()

    check(part1(part1TestInput).also { println("Part 1 Test: $it") } == 13)
    check(part2(part1TestInput).also { println("Part 2 Test: $it") } == 30)

    val input = readInput("inputDay4")
    part1(input).also { println("Part 1: $it") }
    part2(input).also { println("Part 2: $it") }
}