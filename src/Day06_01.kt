fun main() {
    fun Iterable<Long>.countWaysToBreakTheRecord(time: Long, distance: Long): Int =
        count { sec -> sec * (time - sec) > distance }

    fun part1(almanac: List<String>): Int {
        val times = almanac[0].splitNTrim(':')[1].split("\\s+".toRegex()).map { it.toLong() }
        val distances = almanac[1].splitNTrim(':')[1].split("\\s+".toRegex()).map { it.toLong() }

        return times.withIndex().map { (ind, time) ->
            val distance = distances[ind]
            (1 until time).countWaysToBreakTheRecord(time, distance)
        }.reduce(Int::times)
    }

    fun part2(almanac: List<String>): Int {
        val time = almanac[0].splitNTrim(':')[1].replace("\\s+".toRegex(), "").toLong()
        val distance = almanac[1].splitNTrim(':')[1].replace("\\s+".toRegex(), "").toLong()
        return (14L..time - 14).countWaysToBreakTheRecord(time, distance)
    }

    val part1TestInput = """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent().lines()

    check(part1(part1TestInput).also { println("Part 1 Test: $it") } == 288)
    check(part2(part1TestInput).also { println("Part 2 Test: $it") } == 71503)

    val input = readInput("inputDay6")
    part1(input).also { println("Part 1: $it") }
    part2(input).also { println("Part 2: $it") }
}