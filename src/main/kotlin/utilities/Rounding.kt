package utilities

/**
 * Round [int] up to a multiple of [base].
 */
fun roundUpToMultiple(int: Int, base: Int): Int {
    return if (int == 0) {
        0
    } else {
        (int / base + 1) * base
    }
}