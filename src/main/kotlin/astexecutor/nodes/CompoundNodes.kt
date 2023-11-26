package astexecutor.nodes


data class StatementSequenceNode(val left: ASTNode, val right: ASTNode) : ASTNode

data class PrintNode(val valueNode: ASTNode): ASTNode

data class AssignNode(val toAssign: VariableNode, val valueNode: ASTNode) : ASTNode