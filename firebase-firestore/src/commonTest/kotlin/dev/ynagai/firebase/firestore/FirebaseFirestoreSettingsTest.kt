package dev.ynagai.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class FirebaseFirestoreSettingsTest {

    @Test
    fun defaultValues() {
        val settings = FirebaseFirestoreSettings()
        assertEquals("firestore.googleapis.com", settings.host)
        assertTrue(settings.isSslEnabled)
        assertTrue(settings.isPersistenceEnabled)
        assertEquals(100L * 1024 * 1024, settings.cacheSizeBytes)
    }

    @Test
    fun customHost() {
        val settings = firestoreSettings {
            host = "10.0.2.2:8080"
        }
        assertEquals("10.0.2.2:8080", settings.host)
    }

    @Test
    fun sslDisabled() {
        val settings = firestoreSettings {
            isSslEnabled = false
        }
        assertFalse(settings.isSslEnabled)
    }

    @Test
    fun persistenceDisabled() {
        val settings = firestoreSettings {
            isPersistenceEnabled = false
        }
        assertFalse(settings.isPersistenceEnabled)
    }

    @Test
    fun customCacheSize() {
        val settings = firestoreSettings {
            cacheSizeBytes = 50L * 1024 * 1024
        }
        assertEquals(50L * 1024 * 1024, settings.cacheSizeBytes)
    }

    @Test
    fun cacheSizeUnlimited() {
        val settings = firestoreSettings {
            cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        }
        assertEquals(-1L, settings.cacheSizeBytes)
    }

    @Test
    fun dslBuildsAllProperties() {
        val settings = firestoreSettings {
            host = "localhost:8080"
            isSslEnabled = false
            isPersistenceEnabled = false
            cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        }
        assertEquals("localhost:8080", settings.host)
        assertFalse(settings.isSslEnabled)
        assertFalse(settings.isPersistenceEnabled)
        assertEquals(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED, settings.cacheSizeBytes)
    }

    @Test
    fun dslDefaultValues() {
        val settings = firestoreSettings { }
        assertEquals(FirebaseFirestoreSettings(), settings)
    }

    @Test
    fun equality() {
        val a = firestoreSettings {
            host = "custom-host"
            isSslEnabled = false
        }
        val b = firestoreSettings {
            host = "custom-host"
            isSslEnabled = false
        }
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun inequality() {
        val a = firestoreSettings { host = "host-a" }
        val b = firestoreSettings { host = "host-b" }
        assertNotEquals(a, b)
    }

    @Test
    fun copy() {
        val original = firestoreSettings {
            host = "original-host"
            isSslEnabled = true
        }
        val copy = original.copy(host = "new-host")
        assertEquals("new-host", copy.host)
        assertTrue(copy.isSslEnabled)
    }

    @Test
    fun constants() {
        assertEquals(-1L, FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
        assertEquals("firestore.googleapis.com", FirebaseFirestoreSettings.DEFAULT_HOST)
    }
}
