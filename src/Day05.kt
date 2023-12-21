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
    data class MapRange(val source: LongRange, val dest: LongRange)

    fun part1(almanac: List<String>): Long {
        val seeds = almanac[0].split(": ")[1].split(' ').map(String::toLong)

        val mapRanges = mutableMapOf<MapType, List<MapRange>>()

        var i = 2
        fun getMap(): List<MapRange> = buildList {
            i++
            while (!almanac.getOrNull(i).isNullOrBlank()) {
                val (destStart, sourceStart, range) = almanac[i++].split(' ').map(String::toLong)
                add(
                    MapRange(
                        source = sourceStart until (sourceStart + range),
                        dest = destStart until (destStart + range),
                    )
                )
            }
        }

        while (i < almanac.size) {
            val mapTitle = almanac[i]

            MapType.entries.find { mapTitle.startsWith(it.name) }
                ?.let { mapType -> mapRanges[mapType] = getMap().also { println("$mapType $it") } }
                ?: i++
        }

        fun Long.correspondingNumber(from: List<MapRange>): Long =
            from.find { this in it.source }?.let { it.dest.elementAt(it.source.indexOf(this)) } ?: this

        fun Long.getLocation(): Long =
            MapType.entries.fold(this) { acc, mapType -> acc.correspondingNumber(mapRanges[mapType]!!) }

        return seeds.minOf { seed -> seed.getLocation().also { println("$seed $it") } }
    }

    fun part2(almanac: List<String>): Long {
        return 0
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
//    check(part2(part1TestInput).also { println("Part 2 Test: $it") } == 30)

    val input = readInput("inputDay5")
    part1(input).also { println("Part 1: $it") }
//    part2(input).also { println("Part 2: $it") }
}