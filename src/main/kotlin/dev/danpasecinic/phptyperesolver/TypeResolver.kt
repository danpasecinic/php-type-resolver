package dev.danpasecinic.phptyperesolver

private val MIXED = TypeFactory.createType("mixed")
private val WHITESPACE = "\\s+".toRegex()

fun inferTypeFromDoc(variable: PhpVariable): PhpType {
    val docBlock = variable.getDocBlock() ?: return MIXED
    val tags = docBlock.getTagsByName("var")

    if (tags.isEmpty()) return MIXED

    val variableName = variable.getName()
    var unnamedType: String? = null

    for (tag in tags) {
        val parts = tag.getValue().trim().split(WHITESPACE)
        if (parts.isEmpty()) continue

        val typeStr = parts[0]
        if (typeStr.isEmpty()) continue

        if (parts.size >= 2) {
            if (parts[1] == variableName) {
                return parseType(typeStr)
            }
        } else if (unnamedType == null) {
            unnamedType = typeStr
        }
    }

    if (tags.size == 1 && unnamedType != null) {
        return parseType(unnamedType)
    }

    return MIXED
}

private fun parseType(typeStr: String): PhpType {
    val parts = typeStr.split("|").filter { it.isNotEmpty() }

    if (parts.isEmpty()) return MIXED
    if (parts.size == 1) return TypeFactory.createType(parts[0])

    return TypeFactory.createUnionType(parts.map { TypeFactory.createType(it) })
}
