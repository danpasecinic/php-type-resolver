package dev.danpasecinic.phptyperesolver

object TypeFactory {

    fun createType(typeName: String): PhpType = SimpleType(typeName)

    fun createUnionType(types: List<PhpType>): PhpType {
        require(types.isNotEmpty()) { "Cannot create union from empty list" }
        if (types.size == 1) return types.first()
        return UnionType(types)
    }
}
