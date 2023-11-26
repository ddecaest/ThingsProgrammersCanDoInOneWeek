package astexecutor

import astexecutor.nodes.*

fun main() {
    // myVar = "test"
    // a = 0
    // b = 3
    // a = b + 2
    // print a
    // print myVar + b
    val myFirstProgram = StatementSequenceNode(
        StatementSequenceNode(
            AssignNode(VariableNode("myVar"), stringNode("test")),
            StatementSequenceNode(
                AssignNode(VariableNode("a"), intNode(0)),
                AssignNode(VariableNode("b"), intNode(3)),
            )
        ),
        StatementSequenceNode(
            AssignNode(
                VariableNode("a"),
                OperationNode(
                    VariableNode("b"),
                    intNode(2),
                    OperationNode.Operator.PLUS
                )
            ),
            StatementSequenceNode(
                PrintNode(VariableNode("a")),
                PrintNode(
                    OperationNode(
                        VariableNode("myVar"),
                        VariableNode("b"),
                        OperationNode.Operator.PLUS
                    ),
                )
            )
        )
    )

    ASTExecutor.execute(myFirstProgram)
}

private fun intNode(int: Int) = ConstantNode(intValue = int)
private fun stringNode(string: String) = ConstantNode(stringValue = string)
