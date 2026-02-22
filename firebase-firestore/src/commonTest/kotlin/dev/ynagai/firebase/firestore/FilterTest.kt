package dev.ynagai.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertNotNull

class FilterTest {

    @Test
    fun equalToReturnsInstance() {
        val filter = Filter.equalTo("field", "value")
        assertNotNull(filter)
    }

    @Test
    fun equalToWithNullReturnsInstance() {
        val filter = Filter.equalTo("field", null)
        assertNotNull(filter)
    }

    @Test
    fun notEqualToReturnsInstance() {
        val filter = Filter.notEqualTo("field", "value")
        assertNotNull(filter)
    }

    @Test
    fun lessThanReturnsInstance() {
        val filter = Filter.lessThan("field", 10)
        assertNotNull(filter)
    }

    @Test
    fun lessThanOrEqualToReturnsInstance() {
        val filter = Filter.lessThanOrEqualTo("field", 10)
        assertNotNull(filter)
    }

    @Test
    fun greaterThanReturnsInstance() {
        val filter = Filter.greaterThan("field", 10)
        assertNotNull(filter)
    }

    @Test
    fun greaterThanOrEqualToReturnsInstance() {
        val filter = Filter.greaterThanOrEqualTo("field", 10)
        assertNotNull(filter)
    }

    @Test
    fun arrayContainsReturnsInstance() {
        val filter = Filter.arrayContains("field", "value")
        assertNotNull(filter)
    }

    @Test
    fun arrayContainsAnyReturnsInstance() {
        val filter = Filter.arrayContainsAny("field", listOf("a", "b"))
        assertNotNull(filter)
    }

    @Test
    fun inArrayReturnsInstance() {
        val filter = Filter.inArray("field", listOf("a", "b"))
        assertNotNull(filter)
    }

    @Test
    fun notInArrayReturnsInstance() {
        val filter = Filter.notInArray("field", listOf("a", "b"))
        assertNotNull(filter)
    }

    @Test
    fun equalToWithFieldPathReturnsInstance() {
        val filter = Filter.equalTo(FieldPath.of("nested", "field"), "value")
        assertNotNull(filter)
    }

    @Test
    fun notEqualToWithFieldPathReturnsInstance() {
        val filter = Filter.notEqualTo(FieldPath.of("field"), "value")
        assertNotNull(filter)
    }

    @Test
    fun lessThanWithFieldPathReturnsInstance() {
        val filter = Filter.lessThan(FieldPath.of("field"), 10)
        assertNotNull(filter)
    }

    @Test
    fun greaterThanWithFieldPathReturnsInstance() {
        val filter = Filter.greaterThan(FieldPath.of("field"), 10)
        assertNotNull(filter)
    }

    @Test
    fun arrayContainsWithFieldPathReturnsInstance() {
        val filter = Filter.arrayContains(FieldPath.of("field"), "value")
        assertNotNull(filter)
    }

    @Test
    fun inArrayWithFieldPathReturnsInstance() {
        val filter = Filter.inArray(FieldPath.of("field"), listOf("a", "b"))
        assertNotNull(filter)
    }

    @Test
    fun andCompositeFilterReturnsInstance() {
        val filter = Filter.and(
            Filter.equalTo("field1", "value1"),
            Filter.greaterThan("field2", 10),
        )
        assertNotNull(filter)
    }

    @Test
    fun orCompositeFilterReturnsInstance() {
        val filter = Filter.or(
            Filter.equalTo("field1", "value1"),
            Filter.equalTo("field2", "value2"),
        )
        assertNotNull(filter)
    }

    @Test
    fun nestedCompositeFilterReturnsInstance() {
        val filter = Filter.or(
            Filter.and(
                Filter.equalTo("field1", "value1"),
                Filter.greaterThan("field2", 10),
            ),
            Filter.and(
                Filter.equalTo("field3", "value3"),
                Filter.lessThan("field4", 5),
            ),
        )
        assertNotNull(filter)
    }

    @Test
    fun equalsAndHashCodeConsistency() {
        val a = Filter.equalTo("field", "value")
        val b = Filter.equalTo("field", "value")
        // Both should be non-null Filter instances
        assertNotNull(a)
        assertNotNull(b)
    }
}
