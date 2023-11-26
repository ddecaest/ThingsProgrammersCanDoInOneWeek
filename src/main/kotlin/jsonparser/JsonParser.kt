package jsonparser

import java.lang.Exception
import java.lang.IllegalStateException

object JsonParser {

    fun offer(content: String): Map<String, Any> {
        return offerInternal(content).data
    }

    private fun offerInternal(content: String): ParsingStep {
        var state = ParsingStep(content, mutableMapOf(), OpenCurlyBraceTokenParserState)
        while (state.newState != null) {
            state = state.newState?.consume(state)!!
        }
        return state
    }

    interface TokenParserState {
        fun consume(state: ParsingStep): ParsingStep
    }

    object OpenCurlyBraceTokenParserState : TokenParserState {

        private val jsonStart = Regex("^\\s*\\{")

        override fun consume(state: ParsingStep): ParsingStep {
            val isJsonStart = jsonStart.find(state.content) ?: throw Exception("Expected { at start")
            return ParsingStep(
                content = state.content.drop(isJsonStart.value.length),
                data = state.data,
                newState = PostOpenCurlyBraceTokenParserState
            )
        }
    }

    object PostOpenCurlyBraceTokenParserState : TokenParserState {

        private val jsonEnd = Regex("^\\s*}")

        private val fieldStart = Regex("^\\s*'([a-zA-Z]+)'\\s*:\\s*")
        private val stringValue = Regex("^'([a-zA-Z0-9]+)'")
        private val booleanValue = Regex("^((true)|(false))")
        private val numberValue = Regex("^[0-9]+\\.?[0-9]*")

        override fun consume(state: ParsingStep): ParsingStep {
            val isJsonEnd = jsonEnd.find(state.content)
            if (isJsonEnd != null) {
                return ParsingStep("", state.data, null)
            }

            val isFieldStart = fieldStart.find(state.content)
                ?: throw IllegalStateException("Expected json end or field start!")

            val newData = state.data
            val key = isFieldStart.groupValues[1]
            val contentPastField = state.content.drop(isFieldStart.value.length)

            val isStringValue = stringValue.find(contentPastField)
            if (isStringValue != null) {
                newData[key] = isStringValue.groupValues[1]
                return ParsingStep(contentPastField.drop(isStringValue.value.length), newData, CommaOrJsonEndConsumer)
            }
            val isBooleanValue = booleanValue.find(contentPastField)
            if (isBooleanValue != null) {
                newData[key] = isBooleanValue.value.toBoolean()
                return ParsingStep(contentPastField.drop(isBooleanValue.value.length), newData, CommaOrJsonEndConsumer)
            }
            val isNumberValue = numberValue.find(contentPastField)
            if (isNumberValue != null) {
                newData[key] = isNumberValue.value.toDouble() // I could check if it's an int or double here
                return ParsingStep(contentPastField.drop(isNumberValue.value.length), newData, CommaOrJsonEndConsumer)
            }
            // It must be an object
            val parsingStepAfterObject = offerInternal(contentPastField)
            newData[key] = parsingStepAfterObject.data
            return ParsingStep(parsingStepAfterObject.content, newData, CommaOrJsonEndConsumer)
        }
    }

    object CommaOrJsonEndConsumer : TokenParserState {

        private val jsonEnd = Regex("^\\s+}")

        override fun consume(state: ParsingStep): ParsingStep {
            val isJsonEnd = jsonEnd.find(state.content)
            if (isJsonEnd != null) {
                return ParsingStep(state.content.drop(isJsonEnd.value.length), state.data, null)
            }
            if (state.content[0] == ',') {
                return ParsingStep(state.content.drop(1), state.data, PostOpenCurlyBraceTokenParserState)
            }
            throw IllegalStateException("Expected comma or ending curly brace")
        }
    }

    data class ParsingStep(
        val content: String,
        val data: MutableMap<String, Any>,
        val newState: TokenParserState?
    )
}