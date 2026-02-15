package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SchemaTest {

    @Test
    fun stringSchemaHasCorrectType() {
        val schema = Schema.string(description = "A name")
        assertEquals(SchemaType.STRING, schema.type)
        assertEquals("A name", schema.description)
        assertFalse(schema.nullable)
        assertNull(schema.format)
    }

    @Test
    fun stringSchemaSupportsNullable() {
        val schema = Schema.string(nullable = true)
        assertTrue(schema.nullable)
    }

    @Test
    fun integerSchemaHasInt32Format() {
        val schema = Schema.integer(description = "Age")
        assertEquals(SchemaType.INTEGER, schema.type)
        assertEquals("int32", schema.format)
        assertEquals("Age", schema.description)
    }

    @Test
    fun longSchemaHasInt64Format() {
        val schema = Schema.long(description = "Big number")
        assertEquals(SchemaType.INTEGER, schema.type)
        assertEquals("int64", schema.format)
    }

    @Test
    fun floatSchemaHasFloatFormat() {
        val schema = Schema.float(description = "Score")
        assertEquals(SchemaType.NUMBER, schema.type)
        assertEquals("float", schema.format)
    }

    @Test
    fun doubleSchemaHasDoubleFormat() {
        val schema = Schema.double()
        assertEquals(SchemaType.NUMBER, schema.type)
        assertEquals("double", schema.format)
    }

    @Test
    fun booleanSchemaHasCorrectType() {
        val schema = Schema.boolean(description = "Active flag")
        assertEquals(SchemaType.BOOLEAN, schema.type)
        assertEquals("Active flag", schema.description)
    }

    @Test
    fun arraySchemaContainsItems() {
        val items = Schema.string()
        val schema = Schema.array(items = items, description = "Tags")
        assertEquals(SchemaType.ARRAY, schema.type)
        assertEquals(items, schema.items)
        assertEquals("Tags", schema.description)
    }

    @Test
    fun objectSchemaContainsProperties() {
        val props = mapOf(
            "name" to Schema.string(),
            "age" to Schema.integer(),
        )
        val schema = Schema.obj(
            properties = props,
            requiredProperties = listOf("name"),
            description = "User",
        )
        assertEquals(SchemaType.OBJECT, schema.type)
        assertEquals(props, schema.properties)
        assertEquals(listOf("name"), schema.requiredProperties)
    }

    @Test
    fun enumerationSchemaHasEnumFormat() {
        val schema = Schema.enumeration(
            values = listOf("RED", "GREEN", "BLUE"),
            description = "Color",
        )
        assertEquals(SchemaType.STRING, schema.type)
        assertEquals("enum", schema.format)
        assertEquals(listOf("RED", "GREEN", "BLUE"), schema.enumValues)
    }

    @Test
    fun dataClassEquality() {
        val a = Schema.string(description = "test")
        val b = Schema.string(description = "test")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun dataClassCopy() {
        val original = Schema.string(description = "original")
        val copied = original.copy(description = "copied")
        assertEquals("copied", copied.description)
        assertEquals(SchemaType.STRING, copied.type)
    }
}
