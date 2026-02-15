package dev.ynagai.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class AggregateFieldTest {

    @Test
    fun countReturnsInstance() {
        val field = AggregateField.count()
        assertNotNull(field)
    }

    @Test
    fun sumReturnsInstance() {
        val field = AggregateField.sum("population")
        assertNotNull(field)
    }

    @Test
    fun averageReturnsInstance() {
        val field = AggregateField.average("population")
        assertNotNull(field)
    }

    @Test
    fun sumWithDifferentFieldsProducesDistinctInstances() {
        val a = AggregateField.sum("fieldA")
        val b = AggregateField.sum("fieldB")
        assertNotEquals(a, b)
    }

    @Test
    fun averageWithDifferentFieldsProducesDistinctInstances() {
        val a = AggregateField.average("fieldA")
        val b = AggregateField.average("fieldB")
        assertNotEquals(a, b)
    }

    @Test
    fun aggregateSourceHasServerValue() {
        val values = AggregateSource.values()
        assertEquals(1, values.size)
        assertEquals(AggregateSource.SERVER, values[0])
    }
}
