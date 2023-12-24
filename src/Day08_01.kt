fun main() {
    fun List<String>.instructions() = this[0].map { if (it == 'L') 0 else 1 }

    fun List<String>.network(): Map<String, List<String>> =
        drop(2).map { it.split(" = ") }
            .associate { (node, pathsString) ->
                node to pathsString.removeSurrounding("(", ")").split(", ")
            }

    fun findPathLength(
        startNode: String,
        instructions: List<Int>,
        network: Map<String, List<String>>,
        isTarget: (String) -> Boolean,
    ): Long {
        var currentNode = startNode
        var cnt = 0L
        for (instruction in instructions.asCircularSequence()) {
            cnt++
            currentNode = network[currentNode]!![instruction]
            if (isTarget(currentNode))
                break
        }
        return cnt
    }

    fun part1(input: List<String>): Long {
        val network = input.network()
        val instructions = input.instructions()
        val startingNode = "AAA"
        return findPathLength(startingNode, instructions, network, isTarget = { it == "ZZZ" })
    }

    fun part2(input: List<String>): Long {
        val network = input.network()
        val instructions = input.instructions()
        val startingNodes = network.keys.filter { it.last() == 'A' }
        return startingNodes
            .map { startingNode ->
                findPathLength(
                    startingNode,
                    instructions,
                    network,
                    isTarget = { it.last() == 'Z' },
                )
            }
            .lcm()
    }

    val part1TestInput1 = """
            RL

            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent().lines()

    val part1TestInput2 = """
            LLR

            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent().lines()

    val part2TestInput = """
            LR

            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent().lines()

    check(part1(part1TestInput1).also { println("Part 1 Test 1: $it") } == 2L)
    check(part1(part1TestInput2).also { println("Part 1 Test 2: $it") } == 6L)
    check(part2(part2TestInput).also { println("Part 2 Test: $it") } == 6L)

    val input = readInput("inputDay8")
    part1(input).also { println("Part 1: $it") }
    part2(input).also { println("Part 2: $it") }
}
