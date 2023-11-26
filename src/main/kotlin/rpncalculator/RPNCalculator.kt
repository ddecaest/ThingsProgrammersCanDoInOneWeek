package rpncalculator

import kotlin.math.pow

object RPNCalculator {

    private val removeUnnecessaryWhiteSpaceRegex = Regex("\\s+")

    fun calculate(rpnInstructions: String): Double {
        val state = RPNCalculatorState()

        val instructionList = parse(rpnInstructions)
        instructionList.forEach { state.process(it) }

        return state.getValue()
    }

    private fun parse(rpnInstructions: String): List<Token> {
        return rpnInstructions.replace(removeUnnecessaryWhiteSpaceRegex, " ")
            .trim()
            .split(" ")
            .map { RPNTokenParser.parse(it) }
    }

    private class RPNCalculatorState {

        val stackOfStates = ArrayDeque<Double?>()

        var value: Double? = null
        var secondOperand: Double? = null

        fun process(it: Token) {
            if (value == null) {
                value = it.number!!
                return
            }
            if (secondOperand == null) {
                secondOperand = it.number!!
                return
            }

            if (it.number != null) {
                stackOfStates.add(value)
                value = secondOperand
                secondOperand = it.number
                return
            }

            value = when (it.operation!!) {
                Operation.PLUS -> value!! + secondOperand!!
                Operation.MINUS -> value!! - secondOperand!!
                Operation.DIVIDE -> value!! / secondOperand!!
                Operation.MULTIPLY -> value!! * secondOperand!!
                Operation.POWER -> value!!.pow(secondOperand!!)
            }
            secondOperand = null

            if(stackOfStates.isNotEmpty()) {
                secondOperand = value
                value = stackOfStates.removeLast()
            }
        }

        fun getValue(): Double {
            return value!!
        }
    }

    private object RPNTokenParser {

        fun parse(instruction: String): Token {
            val matchingOperation = Operation.values().find { it.token == instruction }
            if (matchingOperation != null) {
                return Token(matchingOperation, null)
            }

            val asDouble = instruction.toDouble()
            return Token(null, asDouble)
        }
    }

    private data class Token(val operation: Operation?, val number: Double?)

    private enum class Operation(val token: String) {
        PLUS("+"),
        MINUS("-"),
        DIVIDE("/"),
        MULTIPLY("*"),
        POWER("^")
    }
}