package dev.ynagai.firebase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FirebaseOptionsTest {

    @Test
    fun propertiesAreAccessible() {
        val options = FirebaseOptions(
            apiKey = "test-api-key",
            applicationId = "1:123:android:abc",
            databaseUrl = "https://test.firebaseio.com",
            gaTrackingId = "UA-12345",
            gcmSenderId = "123456",
            storageBucket = "test.appspot.com",
            projectId = "test-project",
        )
        assertEquals("test-api-key", options.apiKey)
        assertEquals("1:123:android:abc", options.applicationId)
        assertEquals("https://test.firebaseio.com", options.databaseUrl)
        assertEquals("UA-12345", options.gaTrackingId)
        assertEquals("123456", options.gcmSenderId)
        assertEquals("test.appspot.com", options.storageBucket)
        assertEquals("test-project", options.projectId)
    }

    @Test
    fun optionalPropertiesDefaultToNull() {
        val options = FirebaseOptions(
            apiKey = "key",
            applicationId = "appId",
        )
        assertNull(options.databaseUrl)
        assertNull(options.gaTrackingId)
        assertNull(options.gcmSenderId)
        assertNull(options.storageBucket)
        assertNull(options.projectId)
    }

    @Test
    fun equalOptionsAreEqual() {
        val o1 = FirebaseOptions(apiKey = "key", applicationId = "appId", projectId = "proj")
        val o2 = FirebaseOptions(apiKey = "key", applicationId = "appId", projectId = "proj")
        assertEquals(o1, o2)
    }

    @Test
    fun differentOptionsAreNotEqual() {
        val o1 = FirebaseOptions(apiKey = "key1", applicationId = "appId")
        val o2 = FirebaseOptions(apiKey = "key2", applicationId = "appId")
        assertNotEquals(o1, o2)
    }

    @Test
    fun hashCodeIsConsistent() {
        val o1 = FirebaseOptions(apiKey = "key", applicationId = "appId", storageBucket = "bucket")
        val o2 = FirebaseOptions(apiKey = "key", applicationId = "appId", storageBucket = "bucket")
        assertEquals(o1.hashCode(), o2.hashCode())
    }

    @Test
    fun dataCopyWorks() {
        val original = FirebaseOptions(apiKey = "key", applicationId = "appId")
        val copy = original.copy(projectId = "new-project")
        assertEquals("key", copy.apiKey)
        assertEquals("appId", copy.applicationId)
        assertEquals("new-project", copy.projectId)
    }

    @Test
    fun toStringContainsClassName() {
        val options = FirebaseOptions(apiKey = "key", applicationId = "appId")
        val str = options.toString()
        assertTrue(str.contains("FirebaseOptions"))
        assertTrue(str.contains("apiKey=key"))
    }
}
