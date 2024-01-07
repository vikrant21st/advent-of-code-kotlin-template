fun main() {
    val rock = '#'
    val roundRock = 'O'
    val space = '.'

    fun Array<CharArray>.load() = indices.sumOf { row ->
        this[row].filter { it == 'O' }.sumOf { size - row }
    }

    val tilt = object {
        private fun reachedEnd(c: Char) = c == rock || c == roundRock

        fun toNorth(matrix: Array<CharArray>) {
            val rows = matrix.indices.drop(1)
            matrix[0].indices.forEach { col ->
                for (row in rows) {
                    if (matrix[row][col] != roundRock) continue

                    for (traversingRow in (row - 1) downTo 0) {
                        if (reachedEnd(matrix[traversingRow][col])) break

                        matrix[traversingRow][col] = roundRock
                        matrix[traversingRow + 1][col] = space
                    }
                }
            }
        }

        fun toSouth(matrix: Array<CharArray>) {
            val rows = matrix.indices.reversed().drop(1)
            matrix[0].indices.forEach { col ->
                for (row in rows) {
                    if (matrix[row][col] != roundRock) continue

                    for (traversingRow in (row + 1)..matrix.lastIndex) {
                        if (reachedEnd(matrix[traversingRow][col])) break

                        matrix[traversingRow][col] = roundRock
                        matrix[traversingRow + 1][col] = space
                    }
                }
            }
        }

        fun toWest(matrix: Array<CharArray>) {
            val cols = matrix[0].indices.drop(1)
            matrix.indices.forEach { row ->
                for (col in cols) {
                    if (matrix[row][col] != roundRock) continue

                    for (traversingCol in (col - 1) downTo 0) {
                        if (reachedEnd(matrix[row][traversingCol])) break

                        matrix[row][traversingCol] = roundRock
                        matrix[row][traversingCol + 1] = space
                    }
                }
            }
        }

        fun toEast(matrix: Array<CharArray>) {
            val cols = matrix[0].indices.reversed().drop(1)
            matrix.indices.forEach { row ->
                for (col in cols) {
                    if (matrix[row][col] != roundRock) continue

                    for (traversingCol in (col + 1)..(matrix[row].lastIndex)) {
                        if (reachedEnd(matrix[row][traversingCol])) break

                        matrix[row][traversingCol] = roundRock
                        matrix[row][traversingCol + 1] = space
                    }
                }
            }
        }
    }

    fun spin(matrix: Array<CharArray>) {
        tilt.toNorth(matrix)
        tilt.toWest(matrix)
        tilt.toSouth(matrix)
        tilt.toEast(matrix)
    }

    fun findPattern(array: IntArray, patternSizes: IntProgression): List<Int>? =
        patternSizes.find { patternSize ->
            (0..<patternSize).all { i ->
                array[array.lastIndex - i] == array[array.lastIndex - i - patternSize]
            }
        }?.let { array.takeLast(it) }

    fun part1(matrix: Array<CharArray>): Int {
        tilt.toNorth(matrix)
        return matrix.load()
    }

    fun part2(matrix: Array<CharArray>): Int {
        val loads = IntArray(1_000)
        repeat(loads.size) { cycle ->
            spin(matrix)
            loads[cycle] = matrix.load()
        }
        val pattern = findPattern(loads, matrix.size downTo (matrix.size.div(2)))
            ?: error("No pattern found")
        println("Pattern: $pattern")
        return when (val it = (1_000_000_000 - loads.size) % pattern.size) {
            0 -> pattern.last()
            else -> pattern[it - 1]
        }
    }

    val part1TestInput = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent().lines()

    check(part1(part1TestInput.map { it.toCharArray() }.toTypedArray())
        .also { println("Part 1 Test: $it") } == 136)

    part1TestInput.map { it.toCharArray() }.toTypedArray().let { testMatrixInput ->
        spin(testMatrixInput).also { check(testMatrixInput.load() == 87) }
        spin(testMatrixInput).also { check(testMatrixInput.load() == 69) }
        spin(testMatrixInput).also { check(testMatrixInput.load() == 69) }
    }

    check(part2(part1TestInput.map { it.toCharArray() }.toTypedArray())
        .also { println("Part 2 Test: $it") } == 46)

    val input = readInput("inputDay14").map { it.toCharArray() }.toTypedArray()
    part1(input).also { println("Part 1: $it") }
    part2(input).also { println("Part 2: $it") }
}