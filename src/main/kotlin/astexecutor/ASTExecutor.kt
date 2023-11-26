package astexecutor

import astexecutor.nodes.*
import astexecutor.nodes.OperationNode.Operator
import java.lang.IllegalStateException

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
            is VariableNode -> return programState.variables[astNode.variableName]
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
            is OperationNode -> return evaluateMathOp(astNode, programState)
            else -> return null
        }
    }

    private fun evaluateMathOp(astNode: OperationNode, programState: ProgramState): Any {
        val leftValue = evaluate(astNode.leftOperand, programState)
        val rightValue = evaluate(astNode.rightOperand, programState)

        if (leftValue is String) {
            if (astNode.operator != Operator.PLUS) {
                throw IllegalStateException("The only valid operator on strings is PLUS")
            }
            return leftValue + rightValue.toString()
        }
        if (leftValue is Boolean) {
            if (rightValue !is Boolean) {
                throw IllegalStateException("Expecting right operand of a boolean to also be a boolean")
            }
            return when (astNode.operator) {
                Operator.AND -> leftValue && rightValue
                Operator.OR -> leftValue || rightValue
                Operator.EQUALS -> leftValue == rightValue
                else -> throw IllegalStateException("The only valid operators on boolean are AND,OR,EQUALS")
            }
        }
        if (leftValue is Int) {
            if (rightValue !is Int) {
                throw IllegalStateException("Expecting right operand of an int to be an int")
            }
            return when (astNode.operator) {
                Operator.PLUS -> leftValue + rightValue
                Operator.MINUS -> leftValue - rightValue
                Operator.MULTIPLY -> leftValue * rightValue
                Operator.DIVIDE -> leftValue / rightValue
                else -> throw IllegalStateException("The only valid operators on int are PLUS,MINUS,MULTIPLY,DIVIDE")
            }
        }
        if (leftValue is Double) {
            if (rightValue !is Double) {
                throw IllegalStateException("Expecting right operand of a double to be a double")
            }
            return when (astNode.operator) {
                Operator.PLUS -> leftValue + rightValue
                Operator.MINUS -> leftValue - rightValue
                Operator.MULTIPLY -> leftValue * rightValue
                Operator.DIVIDE -> leftValue / rightValue
                else -> throw IllegalStateException("The only valid operators on double are PLUS,MINUS,MULTIPLY,DIVIDE")
            }
        }

        throw IllegalStateException("Unexpected left value type!")
    }

    private class ProgramState {
        val variables = mutableMapOf<String, Any?>()
    }
}