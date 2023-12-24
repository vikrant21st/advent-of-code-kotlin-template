import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun CharSequence.reversedWindowedSequence(size: Int, step: Int = 1, partialWindows: Boolean = false): Sequence<String> =
    reversedWindowedSequence(size, step, partialWindows) { it.toString() }

fun <R> CharSequence.reversedWindowedSequence(
    size: Int,
    step: Int = 1,
    partialWindows: Boolean = false,
    transform: (CharSequence) -> R,
): Sequence<R> {
    checkWindowSizeStep(size, step)
    val windows = (if (partialWindows) indices else 0 until length - size + 1).reversed() step step
    return windows.asSequence().map { it + 1 }.map { endIndex ->
        val start = endIndex - size
        val coercedStart = if (start < 0) 0 else start
        transform(subSequence(coercedStart, endIndex))
    }
}

private fun checkWindowSizeStep(size: Int, step: Int) {
    require(size > 0 && step > 0) {
        if (size != step)
            "Both size $size and step $step must be greater than zero."
        else
            "size $size must be greater than zero."
    }
}

fun CharSequence.splitNTrim(delimiter: Char, limit: Int = 0): List<String> =
    split(delimiter, limit = limit).map(String::trim)

fun Iterable<Long>.lcm(): Long {
    val minValue = min()
    var lcmN = min()
    while (!all { lcmN % it == 0L })
        lcmN += minValue

    return lcmN
}

fun <T> Iterable<T>.asCircularSequence(): Sequence<T> {
    var itr = iterator()
    if (!itr.hasNext()) emptySequence<T>()
    return generateSequence(itr.next()) {
        if (!itr.hasNext())
            itr = iterator()
        itr.next()
    }
}
