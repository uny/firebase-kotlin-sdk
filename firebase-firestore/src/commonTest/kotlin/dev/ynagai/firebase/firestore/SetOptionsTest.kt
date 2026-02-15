package dev.ynagai.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SetOptionsTest {

    @Test
    fun mergeReturnsMergeInstance() {
        val options = SetOptions.merge()
        assertIs<SetOptions.Merge>(options)
    }

    @Test
    fun mergeFieldsWithListReturnsCorrectFields() {
        val options = SetOptions.mergeFields(listOf("name", "age"))
        assertIs<SetOptions.MergeFields>(options)
        assertEquals(listOf("name", "age"), (options as SetOptions.MergeFields).fields)
    }

    @Test
    fun mergeFieldsWithVarargsReturnsCorrectFields() {
        val options = SetOptions.mergeFields("name", "age")
        assertIs<SetOptions.MergeFields>(options)
        assertEquals(listOf("name", "age"), (options as SetOptions.MergeFields).fields)
    }

    @Test
    fun mergeFieldsWithEmptyListReturnsEmptyFields() {
        val options = SetOptions.mergeFields(emptyList())
        assertIs<SetOptions.MergeFields>(options)
        assertEquals(emptyList(), (options as SetOptions.MergeFields).fields)
    }
}
