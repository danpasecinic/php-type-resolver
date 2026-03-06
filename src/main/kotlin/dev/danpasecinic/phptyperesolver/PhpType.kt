package dev.danpasecinic.phptyperesolver

sealed interface PhpType {
    val name: String
}

data class SimpleType(override val name: String) : PhpType {
    override fun toString(): String = name
}

data class UnionType(val types: List<PhpType>) : PhpType {
    init {
        require(types.size >= 2) { "UnionType requires at least 2 types, got ${types.size}" }
    }

    override val name: String get() = types.joinToString("|") { it.name }
    override fun toString(): String = name
}
