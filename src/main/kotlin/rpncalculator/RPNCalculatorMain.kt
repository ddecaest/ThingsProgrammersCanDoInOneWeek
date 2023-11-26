package rpncalculator

fun main() {
    test("3    4 + ", 7.0)
    test("3 4  -   5 +", 4.0)
    test("3 4 * 5 6 * +", 42.0)
    test("5 3 + 10 * 8 4 + -", 68.0)
}

private fun test(rpnString: String, expectedResult: Double) {
    val actual = RPNCalculator.calculate(rpnString)
    println(actual)
    if (expectedResult != actual) {
        throw IllegalStateException("Test failed, expected $expectedResult got $actual")
    }
}