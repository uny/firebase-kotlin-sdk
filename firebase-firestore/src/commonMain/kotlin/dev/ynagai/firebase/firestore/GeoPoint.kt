package dev.ynagai.firebase.firestore

/**
 * Represents a geographical point with latitude and longitude coordinates.
 *
 * @property latitude The latitude in degrees, between -90 and 90 inclusive.
 * @property longitude The longitude in degrees, between -180 and 180 inclusive.
 */
data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
)
