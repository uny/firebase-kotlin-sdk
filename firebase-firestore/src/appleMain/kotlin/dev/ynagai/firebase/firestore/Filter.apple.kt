package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFilter

@OptIn(ExperimentalForeignApi::class)
actual class Filter internal constructor(internal val apple: FIRFilter) {
    actual companion object {
        actual fun equalTo(field: String, value: Any?): Filter =
            Filter(FIRFilter.filterWhereField(field, isEqualTo = value ?: platform.Foundation.NSNull()))

        actual fun notEqualTo(field: String, value: Any?): Filter =
            Filter(FIRFilter.filterWhereField(field, isNotEqualTo = value ?: platform.Foundation.NSNull()))

        actual fun lessThan(field: String, value: Any): Filter =
            Filter(FIRFilter.filterWhereField(field, isLessThan = value))

        actual fun lessThanOrEqualTo(field: String, value: Any): Filter =
            Filter(FIRFilter.filterWhereField(field, isLessThanOrEqualTo = value))

        actual fun greaterThan(field: String, value: Any): Filter =
            Filter(FIRFilter.filterWhereField(field, isGreaterThan = value))

        actual fun greaterThanOrEqualTo(field: String, value: Any): Filter =
            Filter(FIRFilter.filterWhereField(field, isGreaterThanOrEqualTo = value))

        actual fun arrayContains(field: String, value: Any): Filter =
            Filter(FIRFilter.filterWhereField(field, arrayContains = value))

        actual fun arrayContainsAny(field: String, values: List<Any>): Filter =
            Filter(FIRFilter.filterWhereField(field, arrayContainsAny = values))

        actual fun inArray(field: String, values: List<Any>): Filter =
            Filter(FIRFilter.filterWhereField(field, `in` = values))

        actual fun notInArray(field: String, values: List<Any>): Filter =
            Filter(FIRFilter.filterWhereField(field, notIn = values))

        actual fun equalTo(fieldPath: FieldPath, value: Any?): Filter =
            Filter(FIRFilter.filterWhereFieldPath(fieldPath.apple, isEqualTo = value ?: platform.Foundation.NSNull()))

        actual fun notEqualTo(fieldPath: FieldPath, value: Any?): Filter =
            Filter(FIRFilter.filterWhereFieldPath(fieldPath.apple, isNotEqualTo = value ?: platform.Foundation.NSNull()))

        actual fun lessThan(fieldPath: FieldPath, value: Any): Filter =
            Filter(FIRFilter.filterWhereFieldPath(fieldPath.apple, isLessThan = value))

        actual fun lessThanOrEqualTo(fieldPath: FieldPath, value: Any): Filter =
            Filter(FIRFilter.filterWhereFieldPath(fieldPath.apple, isLessThanOrEqualTo = value))

        actual fun greaterThan(fieldPath: FieldPath, value: Any): Filter =
            Filter(FIRFilter.filterWhereFieldPath(fieldPath.apple, isGreaterThan = value))

        actual fun greaterThanOrEqualTo(fieldPath: FieldPath, value: Any): Filter =
            Filter(FIRFilter.filterWhereFieldPath(fieldPath.apple, isGreaterThanOrEqualTo = value))

        actual fun arrayContains(fieldPath: FieldPath, value: Any): Filter =
            Filter(FIRFilter.filterWhereFieldPath(fieldPath.apple, arrayContains = value))

        actual fun arrayContainsAny(fieldPath: FieldPath, values: List<Any>): Filter =
            Filter(FIRFilter.filterWhereFieldPath(fieldPath.apple, arrayContainsAny = values))

        actual fun inArray(fieldPath: FieldPath, values: List<Any>): Filter =
            Filter(FIRFilter.filterWhereFieldPath(fieldPath.apple, `in` = values))

        actual fun notInArray(fieldPath: FieldPath, values: List<Any>): Filter =
            Filter(FIRFilter.filterWhereFieldPath(fieldPath.apple, notIn = values))

        actual fun and(vararg filters: Filter): Filter =
            Filter(FIRFilter.andFilterWithFilters(filters.map { it.apple }))

        actual fun or(vararg filters: Filter): Filter =
            Filter(FIRFilter.orFilterWithFilters(filters.map { it.apple }))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Filter) return false
        return apple == other.apple
    }

    override fun hashCode(): Int = apple.hash.toInt()

    override fun toString(): String = "Filter(apple=$apple)"
}
