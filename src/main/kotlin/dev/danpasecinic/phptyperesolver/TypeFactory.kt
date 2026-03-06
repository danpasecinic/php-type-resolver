package dev.danpasecinic.phptyperesolver

object TypeFactory {

    fun createType(typeName: String): PhpType = SimpleType(typeName)

    fun createUnionType(types: List<PhpType>): PhpType = UnionType(types)
}
