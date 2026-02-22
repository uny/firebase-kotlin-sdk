package dev.ynagai.firebase.firestore

expect class Filter {
    companion object {
        // Comparison filters (String field)
        fun equalTo(field: String, value: Any?): Filter
        fun notEqualTo(field: String, value: Any?): Filter
        fun lessThan(field: String, value: Any): Filter
        fun lessThanOrEqualTo(field: String, value: Any): Filter
        fun greaterThan(field: String, value: Any): Filter
        fun greaterThanOrEqualTo(field: String, value: Any): Filter
        fun arrayContains(field: String, value: Any): Filter
        fun arrayContainsAny(field: String, values: List<Any>): Filter
        fun inArray(field: String, values: List<Any>): Filter
        fun notInArray(field: String, values: List<Any>): Filter

        // Comparison filters (FieldPath)
        fun equalTo(fieldPath: FieldPath, value: Any?): Filter
        fun notEqualTo(fieldPath: FieldPath, value: Any?): Filter
        fun lessThan(fieldPath: FieldPath, value: Any): Filter
        fun lessThanOrEqualTo(fieldPath: FieldPath, value: Any): Filter
        fun greaterThan(fieldPath: FieldPath, value: Any): Filter
        fun greaterThanOrEqualTo(fieldPath: FieldPath, value: Any): Filter
        fun arrayContains(fieldPath: FieldPath, value: Any): Filter
        fun arrayContainsAny(fieldPath: FieldPath, values: List<Any>): Filter
        fun inArray(fieldPath: FieldPath, values: List<Any>): Filter
        fun notInArray(fieldPath: FieldPath, values: List<Any>): Filter

        // Composite filters
        fun and(vararg filters: Filter): Filter
        fun or(vararg filters: Filter): Filter
    }
}
