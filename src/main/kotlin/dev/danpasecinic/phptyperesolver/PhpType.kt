package dev.danpasecinic.phptyperesolver

interface PhpType {
    val name: String
}

data class SimpleType(override val name: String) : PhpType {
    override fun toString(): String = name
}

data class UnionType(val types: List<PhpType>) : PhpType {
    override val name: String get() = types.joinToString("|") { it.name }
    override fun toString(): String = name
}
