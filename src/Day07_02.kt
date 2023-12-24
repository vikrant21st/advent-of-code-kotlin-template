fun main() {
    data class HandToBid(val cards: String, val bid: Long)

    val cardsAsPerRank = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').reversed()
    val cardsAsPerRankWithJoker = cardsAsPerRank.filterNot { it == 'J' }
    val `Five of a kind` = 7
    val `Four of a kind` = 6
    val `Full house` = 5
    val `Three of a kind` = 4
    val `Two pair` = 3
    val `One pair` = 2
    val `High card` = 1

    fun String.occurrencesOf(c: Char) = count { it == c }
    fun String.isRepeating(c: Char) = occurrencesOf(c) > 1

    fun HandToBid.handType(considerJoker: Boolean): Int {
        val distinctCards = cards.toSet()

        fun Int.promoteIfHasJokers(): Int {
            if (!considerJoker) return this
            val jokers = cards.occurrencesOf('J').takeIf { it > 0 } ?: return this

            return when (this) {
                `Four of a kind` -> `Five of a kind`
                `Full house` -> `Five of a kind`
                `Three of a kind` -> `Four of a kind`
                `Two pair` ->
                    when {
                        jokers == 2 -> `Four of a kind`
                        else -> `Full house`
                    }

                `One pair` -> `Three of a kind`
                `High card` -> `One pair`
                else -> this
            }
        }

        return when {
            distinctCards.size == 1 -> `Five of a kind`

            distinctCards.size == 2 && distinctCards.any { cards.occurrencesOf(it) == 4 } ->
                `Four of a kind`.promoteIfHasJokers()

            distinctCards.size == 2 && distinctCards.all { cards.isRepeating(it) } ->
                `Full house`.promoteIfHasJokers()

            distinctCards.size == 3 && distinctCards.any { cards.occurrencesOf(it) == 3 } ->
                `Three of a kind`.promoteIfHasJokers()

            distinctCards.size == 3 && distinctCards.count { cards.isRepeating(it) } == 2 ->
                `Two pair`.promoteIfHasJokers()

            distinctCards.size == 4 -> `One pair`.promoteIfHasJokers()

            else -> `High card`.promoteIfHasJokers()
        }
    }

    fun parseInput(input: List<String>) =
        input.map { it.split(' ') }.map { (cards, bid) -> HandToBid(cards, bid.toLong()) }

    fun solve(input: List<String>, considerJoker: Boolean): Long {
        val handToBids = parseInput(input)
        val rankings = if (considerJoker) cardsAsPerRankWithJoker else cardsAsPerRank
        val rankedHandToBids = handToBids.sortedWith(
            compareBy<HandToBid> { it.handType(considerJoker) }
                .thenComparing { a, b ->
                    a.cards.indices.firstNotNullOf { ind ->
                        (rankings.indexOf(a.cards[ind]) - rankings.indexOf(b.cards[ind])).takeIf { it != 0 }
                    }
                }
        )
        return rankedHandToBids.withIndex().sumOf { (ind, item) -> (ind + 1) * item.bid }
    }

    fun part1(input: List<String>) = solve(input, considerJoker = false)

    fun part2(input: List<String>) = solve(input, considerJoker = true)

    val part1TestInput = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent().lines()

    check(part1(part1TestInput).also { println("Part 1 Test: $it") } == 6440L)
    check(part2(part1TestInput).also { println("Part 2 Test: $it") } == 5905L)

    val input = readInput("inputDay7")
    part1(input).also { println("Part 1: $it") }
    part2(input).also { println("Part 2: $it") }
}
