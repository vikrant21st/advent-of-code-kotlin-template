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
        val jokers = cards.occurrencesOf('J')

        return when {
            distinctCards.size == 1 -> `Five of a kind`

            distinctCards.size == 2 && distinctCards.any { cards.occurrencesOf(it) == 4 } ->
                `Four of a kind`.let {
                    when {
                        considerJoker && jokers != 0 -> `Five of a kind`
                        else -> it
                    }
                }

            distinctCards.size == 2 && distinctCards.all { cards.isRepeating(it) } ->
                `Full house`.let {
                    when {
                        considerJoker && jokers != 0 -> `Five of a kind`
                        else -> it
                    }
                }

            distinctCards.size == 3 && distinctCards.any { cards.occurrencesOf(it) == 3 } ->
                `Three of a kind`.let {
                    when {
                        considerJoker && jokers != 0 -> `Four of a kind`
                        else -> it
                    }
                }

            distinctCards.size == 3 && distinctCards.count { cards.isRepeating(it) } == 2 ->
                `Two pair`.let {
                    when {
                        considerJoker && jokers == 2 -> `Four of a kind`
                        considerJoker && jokers == 1 -> `Full house`
                        else -> it
                    }
                }

            distinctCards.size == 4 ->
                `One pair`.let {
                    when {
                        considerJoker && jokers != 0 -> `Three of a kind`
                        else -> it
                    }
                }

            else ->
                `High card`.let {
                    when {
                        considerJoker && jokers != 0 -> `One pair`
                        else -> it
                    }
                }

        }
    }

    fun handComparator(considerJoker: Boolean = false) = Comparator<HandToBid> { a, b ->
        val diff = a.handType(considerJoker) - b.handType(considerJoker)
        if (diff != 0) return@Comparator diff
        val rankings = if (considerJoker) cardsAsPerRankWithJoker else cardsAsPerRank
        a.cards.indices.firstNotNullOf { ind ->
            (rankings.indexOf(a.cards[ind]) - rankings.indexOf(b.cards[ind])).takeIf { it != 0 }
        }
    }

    fun parseInput(input: List<String>) =
        input.map { it.split(' ') }.map { (cards, bid) -> HandToBid(cards, bid.toLong()) }

    fun part1(input: List<String>, considerJoker: Boolean = false): Long {
        val handToBids = parseInput(input)
        return handToBids.sortedWith(handComparator(considerJoker)).withIndex()
            .sumOf { (ind, item) -> (ind + 1) * item.bid }
    }

    fun part2(input: List<String>): Long {
        return part1(input, considerJoker = true)
    }

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
