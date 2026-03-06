package dev.danpasecinic.phptyperesolver

interface DocTag {
    fun getValue(): String
}

interface PhpDocBlock {
    fun getTagsByName(tagName: String): List<DocTag>
}

interface PhpVariable {
    fun getDocBlock(): PhpDocBlock?
    fun getName(): String
}
