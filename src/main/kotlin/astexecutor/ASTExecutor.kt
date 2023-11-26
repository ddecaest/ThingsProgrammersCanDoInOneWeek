package astexecutor

import astexecutor.nodes.*

object ASTExecutor {

    fun execute(root: ASTNode) {
        execute(root, ProgramState())
    }

    // Recursive for now
    private fun execute(node: ASTNode, programState: ProgramState) {
        if (node is StatementSequenceNode) {
            // Adjusted by reference is a bit bleh, I could return instead, but it would be the same thing really
            execute(node.left, programState)
            execute(node.right, programState)
        }
        if (node is AssignNode) {
            val variable = node.toAssign.variableName
            val value = evaluate(node.valueNode, programState)
            programState.variables[variable] = value
        }
        if (node is PrintNode) {
            val value = evaluate(node.valueNode, programState)
            println(value)
        }
    }

    private fun evaluate(astNode: ASTNode, programState: ProgramState): Any? {
        when (astNode) {
            is VariableNode -> {
                return programState.variables[astNode.variableName]
            }

            is ConstantNode -> {
                if (astNode.booleanValue != null) {
                    return astNode.booleanValue
                }
                if (astNode.doubleValue != null) {
                    return astNode.doubleValue
                }
                if (astNode.intValue != null) {
                    return astNode.intValue
                }
                return astNode.stringValue
            }

            else -> {
                return null
            }
        }
    }

    private class ProgramState {
        val variables = mutableMapOf<String, Any?>()
    }
}