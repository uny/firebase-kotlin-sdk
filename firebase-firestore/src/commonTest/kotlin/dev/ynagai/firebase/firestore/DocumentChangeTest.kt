package dev.ynagai.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertEquals

class DocumentChangeTest {

    @Test
    fun typeEnumValues() {
        val types = DocumentChange.Type.entries
        assertEquals(3, types.size)
        assertEquals(DocumentChange.Type.ADDED, types[0])
        assertEquals(DocumentChange.Type.MODIFIED, types[1])
        assertEquals(DocumentChange.Type.REMOVED, types[2])
    }

    @Test
    fun typeEnumValueOf() {
        assertEquals(DocumentChange.Type.ADDED, DocumentChange.Type.valueOf("ADDED"))
        assertEquals(DocumentChange.Type.MODIFIED, DocumentChange.Type.valueOf("MODIFIED"))
        assertEquals(DocumentChange.Type.REMOVED, DocumentChange.Type.valueOf("REMOVED"))
    }
}
