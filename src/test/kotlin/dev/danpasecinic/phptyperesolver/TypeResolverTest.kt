package dev.danpasecinic.phptyperesolver

import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

class TypeResolverTest {

    private fun phpVar(name: String) = $$"$$$name"

    @Test
    fun `standard type from unnamed tag`() {
        val variable = mockVariable(phpVar("user"), listOf(mockTag("User")))
        assertEquals(TypeFactory.createType("User"), inferTypeFromDoc(variable))
    }

    @Test
    fun `union type`() {
        val variable = mockVariable(phpVar("id"), listOf(mockTag("string|int")))
        val expected = TypeFactory.createUnionType(
            listOf(TypeFactory.createType("string"), TypeFactory.createType("int"))
        )
        assertEquals(expected, inferTypeFromDoc(variable))
    }

    @Test
    fun `named tag matches variable`() {
        val variable = mockVariable(phpVar("log"), listOf(mockTag("Logger ${phpVar("log")}")))
        assertEquals(TypeFactory.createType("Logger"), inferTypeFromDoc(variable))
    }

    @Test
    fun `named tag does not match variable`() {
        val variable = mockVariable(phpVar("guest"), listOf(mockTag("Admin ${phpVar("adm")}")))
        assertEquals(TypeFactory.createType("mixed"), inferTypeFromDoc(variable))
    }

    @Test
    fun `multiple tags selects matching name`() {
        val variable = mockVariable(
            phpVar("name"),
            listOf(mockTag("int ${phpVar("id")}"), mockTag("string ${phpVar("name")}"))
        )
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
        val variable = mockVariable(phpVar("x"), emptyList())
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
