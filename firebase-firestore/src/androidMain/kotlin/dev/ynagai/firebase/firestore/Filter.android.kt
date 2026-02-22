package dev.ynagai.firebase.firestore

import com.google.firebase.firestore.Filter as AndroidFilter

actual class Filter internal constructor(internal val android: AndroidFilter) {
    actual companion object {
        actual fun equalTo(field: String, value: Any?): Filter =
            Filter(AndroidFilter.equalTo(field, value))

        actual fun notEqualTo(field: String, value: Any?): Filter =
            Filter(AndroidFilter.notEqualTo(field, value))

        actual fun lessThan(field: String, value: Any): Filter =
            Filter(AndroidFilter.lessThan(field, value))

        actual fun lessThanOrEqualTo(field: String, value: Any): Filter =
            Filter(AndroidFilter.lessThanOrEqualTo(field, value))

        actual fun greaterThan(field: String, value: Any): Filter =
            Filter(AndroidFilter.greaterThan(field, value))

        actual fun greaterThanOrEqualTo(field: String, value: Any): Filter =
            Filter(AndroidFilter.greaterThanOrEqualTo(field, value))

        actual fun arrayContains(field: String, value: Any): Filter =
            Filter(AndroidFilter.arrayContains(field, value))

        actual fun arrayContainsAny(field: String, values: List<Any>): Filter =
            Filter(AndroidFilter.arrayContainsAny(field, values))

        actual fun inArray(field: String, values: List<Any>): Filter =
            Filter(AndroidFilter.inArray(field, values))

        actual fun notInArray(field: String, values: List<Any>): Filter =
            Filter(AndroidFilter.notInArray(field, values))

        actual fun equalTo(fieldPath: FieldPath, value: Any?): Filter =
            Filter(AndroidFilter.equalTo(fieldPath.android, value))

        actual fun notEqualTo(fieldPath: FieldPath, value: Any?): Filter =
            Filter(AndroidFilter.notEqualTo(fieldPath.android, value))

        actual fun lessThan(fieldPath: FieldPath, value: Any): Filter =
            Filter(AndroidFilter.lessThan(fieldPath.android, value))

        actual fun lessThanOrEqualTo(fieldPath: FieldPath, value: Any): Filter =
            Filter(AndroidFilter.lessThanOrEqualTo(fieldPath.android, value))

        actual fun greaterThan(fieldPath: FieldPath, value: Any): Filter =
            Filter(AndroidFilter.greaterThan(fieldPath.android, value))

        actual fun greaterThanOrEqualTo(fieldPath: FieldPath, value: Any): Filter =
            Filter(AndroidFilter.greaterThanOrEqualTo(fieldPath.android, value))

        actual fun arrayContains(fieldPath: FieldPath, value: Any): Filter =
            Filter(AndroidFilter.arrayContains(fieldPath.android, value))

        actual fun arrayContainsAny(fieldPath: FieldPath, values: List<Any>): Filter =
            Filter(AndroidFilter.arrayContainsAny(fieldPath.android, values))

        actual fun inArray(fieldPath: FieldPath, values: List<Any>): Filter =
            Filter(AndroidFilter.inArray(fieldPath.android, values))

        actual fun notInArray(fieldPath: FieldPath, values: List<Any>): Filter =
            Filter(AndroidFilter.notInArray(fieldPath.android, values))

        actual fun and(vararg filters: Filter): Filter {
            require(filters.isNotEmpty()) { "Filter.and() requires at least one filter" }
            return Filter(AndroidFilter.and(*filters.map { it.android }.toTypedArray()))
        }

        actual fun or(vararg filters: Filter): Filter {
            require(filters.isNotEmpty()) { "Filter.or() requires at least one filter" }
            return Filter(AndroidFilter.or(*filters.map { it.android }.toTypedArray()))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Filter) return false
        return android == other.android
    }

    override fun hashCode(): Int = android.hashCode()

    override fun toString(): String = "Filter(android=$android)"
}
