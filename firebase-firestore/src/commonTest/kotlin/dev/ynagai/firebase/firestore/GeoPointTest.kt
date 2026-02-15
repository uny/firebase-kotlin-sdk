package dev.ynagai.firebase.firestore

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class GeoPointTest {

    @Test
    fun equalGeoPointsAreEqual() {
        val g1 = GeoPoint(latitude = 35.6762, longitude = 139.6503)
        val g2 = GeoPoint(latitude = 35.6762, longitude = 139.6503)
        assertEquals(g1, g2)
    }

    @Test
    fun differentGeoPointsAreNotEqual() {
        val g1 = GeoPoint(latitude = 35.6762, longitude = 139.6503)
        val g2 = GeoPoint(latitude = 34.6937, longitude = 135.5023)
        assertNotEquals(g1, g2)
    }

    @Test
    fun hashCodeIsConsistent() {
        val g1 = GeoPoint(latitude = 35.6762, longitude = 139.6503)
        val g2 = GeoPoint(latitude = 35.6762, longitude = 139.6503)
        assertEquals(g1.hashCode(), g2.hashCode())
    }

    @Test
    fun dataCopyWorks() {
        val original = GeoPoint(latitude = 35.6762, longitude = 139.6503)
        val copy = original.copy(longitude = 135.5023)
        assertEquals(35.6762, copy.latitude)
        assertEquals(135.5023, copy.longitude)
    }

    @Test
    fun propertiesAreAccessible() {
        val geoPoint = GeoPoint(latitude = -33.8688, longitude = 151.2093)
        assertEquals(-33.8688, geoPoint.latitude)
        assertEquals(151.2093, geoPoint.longitude)
    }
}
