package dev.danpasecinic.phptyperesolver

import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class TypeResolverTest {

    @Test
    fun `standard type from unnamed tag`() {
        val variable = mockVariable("\$user", listOf(mockTag("User")))
        assertEquals(TypeFactory.createType("User"), inferTypeFromDoc(variable))
    }

    @Test
    fun `union type`() {
        val variable = mockVariable("\$id", listOf(mockTag("string|int")))
        val expected = TypeFactory.createUnionType(
            listOf(TypeFactory.createType("string"), TypeFactory.createType("int"))
        )
        assertEquals(expected, inferTypeFromDoc(variable))
    }

    @Test
    fun `named tag matches variable`() {
        val variable = mockVariable("\$log", listOf(mockTag("Logger \$log")))
        assertEquals(TypeFactory.createType("Logger"), inferTypeFromDoc(variable))
    }

    @Test
    fun `named tag does not match variable`() {
        val variable = mockVariable("\$guest", listOf(mockTag("Admin \$adm")))
        assertEquals(TypeFactory.createType("mixed"), inferTypeFromDoc(variable))
    }

    @Test
    fun `multiple tags selects matching name`() {
        val variable = mockVariable("\$name", listOf(mockTag("int \$id"), mockTag("string \$name")))
        assertEquals(TypeFactory.createType("string"), inferTypeFromDoc(variable))
    }

    @Test
    fun `no doc block returns mixed`() {
        val variable = mock<PhpVariable>()
        whenever(variable.getDocBlock()).thenReturn(null)
        assertEquals(TypeFactory.createType("mixed"), inferTypeFromDoc(variable))
    }

    @Test
    fun `no var tags returns mixed`() {
        val variable = mockVariable("\$x", emptyList())
        assertEquals(TypeFactory.createType("mixed"), inferTypeFromDoc(variable))
    }

    private fun mockTag(value: String): DocTag {
        val tag = mock<DocTag>()
        whenever(tag.getValue()).thenReturn(value)
        return tag
    }

    private fun mockVariable(name: String, tags: List<DocTag>): PhpVariable {
        val docBlock = mock<PhpDocBlock>()
        whenever(docBlock.getTagsByName("var")).thenReturn(tags)
        val variable = mock<PhpVariable>()
        whenever(variable.getName()).thenReturn(name)
        whenever(variable.getDocBlock()).thenReturn(docBlock)
        return variable
    }
}
