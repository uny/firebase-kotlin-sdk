package dev.ynagai.firebase.firestore

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.firestore.FIRFieldPath

@OptIn(ExperimentalForeignApi::class)
actual class FieldPath internal constructor(internal val apple: FIRFieldPath) {
    actual companion object {
        actual fun of(vararg fieldNames: String): FieldPath =
            FieldPath(FIRFieldPath(fieldNames.toList()))

        actual fun documentId(): FieldPath =
            FieldPath(FIRFieldPath.documentID())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FieldPath) return false
        return apple == other.apple
    }

    override fun hashCode(): Int = apple.hash.toInt()

    override fun toString(): String = apple.description ?: "FieldPath"
}
