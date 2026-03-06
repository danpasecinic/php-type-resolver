package dev.danpasecinic.phptyperesolver

object TypeFactory {

    fun createType(typeName: String): PhpType = SimpleType(typeName)

    fun createUnionType(types: List<PhpType>): PhpType = when {
        types.isEmpty() -> throw IllegalArgumentException("Cannot create union from empty list")
        types.size == 1 -> types.first()
        else -> UnionType(types)
    }
}
