package astexecutor.nodes

data class VariableNode(val variableName: String) : ASTNode

data class ConstantNode(
    val stringValue: String? = null,
    val booleanValue: Boolean? = null,
    val intValue: Int? = null,
    val doubleValue: Double? = null
) : ASTNode

