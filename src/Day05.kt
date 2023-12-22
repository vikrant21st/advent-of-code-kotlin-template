enum class MapType {
    `seed-to-soil`,
    `soil-to-fertilizer`,
    `fertilizer-to-water`,
    `water-to-light`,
    `light-to-temperature`,
    `temperature-to-humidity`,
    `humidity-to-location`,
}

fun main() {
    data class MapRange(val source: LongRange, val dest: LongRange, val length: Long)

    fun getNextMaxRanges(almanacPortion: List<String>) = buildList {
        for (line in almanacPortion) {
            if (line.getOrNull(0)?.isDigit() != true)
                break

            val (destStart, sourceStart, length) = line.split(' ').map { it.toLong() }
            add(
                MapRange(
                    source = sourceStart until (sourceStart + length),
                    dest = destStart until (destStart + length),
                    length = length,
                )
            )
        }
    }

    fun List<String>.getMapTypeToMapRanges() = buildMap {
        for ((ind, line) in withIndex()) {
            MapType.entries.find { line.startsWith(it.name) }
                ?.let { mapType: MapType ->
                    put(mapType, getNextMaxRanges(drop(ind + 1)))
                }
        }
    }

    fun List<String>.seeds() = this[0].split(": ")[1].split(' ').map { it.toLong() }

    fun List<MapRange>.correspondingNumberFor(n: Long) =
        find { n in it.source}
            ?.let { it.dest.first + (n - it.source.first) }
            ?: n

    fun Long.getLocation(mapRanges: Map<MapType, List<MapRange>>) =
        MapType.entries.map { mapRanges[it]!! }.fold(this) { acc, mapRange -> mapRange.correspondingNumberFor(acc) }

    fun part1(almanac: List<String>): Long =
        almanac.getMapTypeToMapRanges().let { mapRanges: Map<MapType, List<MapRange>> ->
            almanac.seeds().minOf { it.getLocation(mapRanges) }
        }

    fun part2(almanac: List<String>): Long {
        val mapRanges = almanac.getMapTypeToMapRanges()

        fun getMinLocation(longRange: LongRange): Long {
            fun LongRange.getCorrespondingRanges(mapType: MapType): List<LongRange> {
                val intersectingRanges = mapRanges[mapType]!!.sortedBy { it.source.first }
                val matchingSubRanges = mutableListOf<LongRange>()
                var remainingRange: LongRange? = this
                for (it in intersectingRanges) {
                    remainingRange ?: break
                    remainingRange = when {
                        remainingRange.first in it.source && remainingRange.last in it.source -> {
                            val start = it.dest.first + (remainingRange.first - it.source.first)
                            val end = it.dest.last - (it.source.last - remainingRange.last)
                            matchingSubRanges.add(start..end)
                            null
                        }

                        remainingRange.first in it.source -> {
                            val start = it.dest.first + (remainingRange.first - it.source.first)
                            val end = it.dest.last
                            matchingSubRanges.add(start..end)
                            it.source.last + 1..remainingRange.last
                        }

                        remainingRange.last in it.source -> {
                            val start = it.dest.first
                            val end = it.dest.last - (it.source.last - remainingRange.last)
                            matchingSubRanges.add(start..end)
                            remainingRange.first..<it.source.first
                        }

                        else -> {
                            remainingRange
                        }
                    }
                }
                remainingRange?.let { matchingSubRanges.add(it) }
                return matchingSubRanges
            }

            var finalRanges = listOf(longRange)
            for (mapType in MapType.entries) {
                finalRanges = finalRanges.flatMap { it.getCorrespondingRanges(mapType) }
            }
            return finalRanges.minOf { it.first }
        }

        val seedsRanges =
            almanac.seeds().chunked(2).map { (start, range) -> start until (start + range) }

        return seedsRanges.minOf { range -> getMinLocation(range) }
    }

    val part1TestInput = """
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48
            
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
            
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
            
            water-to-light map:
            88 18 7
            18 25 70
            
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
            
            temperature-to-humidity map:
            0 69 1
            1 0 69
            
            humidity-to-location map:
            60 56 37
            56 93 4
        """.trimIndent().lines()

    check(part1(part1TestInput).also { println("Part 1 Test: $it") } == 35L)
    check(part2(part1TestInput).also { println("Part 2 Test: $it") } == 46L)

    val input = readInput("inputDay5")
    part1(input).also { println("Part 1: $it") }
    part2(input).also { println("Part 2: $it") }
}