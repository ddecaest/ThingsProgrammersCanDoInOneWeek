package jsonparser

import java.lang.IllegalStateException

fun main() {
    test("{ }", mapOf<String, Any>())

    val secondJson = """{
        'key': 'value'    
    }"""
    test(secondJson, mapOf("key" to "value"))

    val thirdJson = """{
        'key': 'value',
        'secondKey' :'value2'
    }"""
    val thirdExpected = mapOf(
        "key" to "value",
        "secondKey" to "value2"
    )
    test(thirdJson, thirdExpected)

    val fourthJson = """{
        'key': 'value',
        'secondKey' :'value2',
        'third':2.353,
        'fourth':     true
    }"""
    val fourthExpected = mapOf(
        "key" to "value",
        "secondKey" to "value2",
        "third" to 2.353,
        "fourth" to true
    )
    test(fourthJson, fourthExpected)

    val fifthJson = """{
        'key': 'value',
        'secondKey' :'value2',
        'third':2.353,
        'someObject': {
            'yes': 'no'
        },
        'fourth':     true
    }"""
    val fifthExpected = mapOf(
        "key" to "value",
        "secondKey" to "value2",
        "third" to 2.353,
        "someObject" to mapOf("yes" to "no"),
        "fourth" to true
    )
    test(fifthJson, fifthExpected)
}

private fun test(json: String, expectedResult: Map<String, Any>) {
    val actual = JsonParser.offer(json)
    println(actual)
    if(expectedResult != actual) {
        throw IllegalStateException("Test failed, expected $expectedResult got $actual")
    }
}