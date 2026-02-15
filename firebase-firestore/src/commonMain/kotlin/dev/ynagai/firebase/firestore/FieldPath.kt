package dev.ynagai.firebase.firestore

/**
 * A [FieldPath] refers to a field in a document. The path may consist of a single field name
 * (referring to a top level field in the document), or a list of field names (referring to a nested
 * field in the document).
 */
expect class FieldPath {
    companion object {
        /**
         * Creates a [FieldPath] from the provided field names.
         *
         * @param fieldNames A list of field names (where the first is the top-level field).
         */
        fun of(vararg fieldNames: String): FieldPath

        /**
         * Returns a special sentinel [FieldPath] to refer to the ID of a document.
         * It can be used in queries to sort or filter by the document ID.
         */
        fun documentId(): FieldPath
    }
}
