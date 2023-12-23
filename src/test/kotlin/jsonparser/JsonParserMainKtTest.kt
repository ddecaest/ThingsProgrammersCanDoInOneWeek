package jsonparser

import kotlin.test.Test
import kotlin.test.assertEquals

class JsonParserMainKtTest {

    @Test
    fun `test with a sample json`() {
        val json = """{
            'key': 'value',
            'secondKey' :'value2',
            'third':2.353,
            'someObject': {
                'yes': 'no'
            },
            'fourth':     true
        }"""
        val expected = mapOf(
            "key" to "value",
            "secondKey" to "value2",
            "third" to 2.353,
            "someObject" to mapOf("yes" to "no"),
            "fourth" to true
        )

        val actual = JsonParser.offer(json)
        println(actual)
        assertEquals(expected, actual)
    }
}