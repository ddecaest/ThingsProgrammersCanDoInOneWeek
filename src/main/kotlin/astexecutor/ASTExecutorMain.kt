package astexecutor

import astexecutor.nodes.*

fun main() {
    // a = 0
    // b = 3
    // a = b
    // print a
    // print b
    // print "aaaaah"
    val myFirstProgram = StatementSequenceNode(
        StatementSequenceNode(
            AssignNode(VariableNode("a"), ConstantNode(intValue = 0)),
            AssignNode(VariableNode("b"), ConstantNode(intValue = 3)),
        ),
        StatementSequenceNode(
            AssignNode(VariableNode("a"), VariableNode("b")),
            StatementSequenceNode(
                PrintNode(VariableNode("a")),
                StatementSequenceNode(
                    PrintNode(VariableNode("b")),
                    PrintNode(ConstantNode("aaaaah"))
                )
            )
        )
    )

    ASTExecutor.execute(myFirstProgram)
}