# Firebase Firestore

Kotlin Multiplatform wrapper for Cloud Firestore. Provides CRUD operations, queries, real-time listeners, transactions, and batched writes — all sharing a single Kotlin API across Android and iOS.

## Installation

```kotlin
implementation("dev.ynagai.firebase:firebase-firestore:<version>")
```

Or via Version Catalog — see the [root README](../README.md#installation).

## Usage

### Get a Firestore Instance

```kotlin
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.firestore.firestore

val db = Firebase.firestore
```

### Add and Read Data

```kotlin
// Add a document
val docRef = db.collection("users").add(
    mapOf("name" to "Alice", "age" to 30L)
)

// Read a document
val snapshot = db.collection("users").document("alice").get()
val name = snapshot.getString("name")
```

### Query Documents

```kotlin
val query = db.collection("users")
    .whereEqualTo("city", "Tokyo")
    .orderBy("age")
    .limit(10)

val results = query.get()
results.forEach { doc ->
    println("${doc.id}: ${doc.data}")
}
```

### Real-time Updates

```kotlin
db.collection("users").document("alice").snapshots.collect { snapshot ->
    println("Current data: ${snapshot.data}")
}
```

### Transactions and Batched Writes

```kotlin
// Transaction
db.runTransaction {
    val snapshot = get(docRef)
    val newCount = (snapshot.getLong("count") ?: 0) + 1
    update(docRef, mapOf("count" to newCount))
}

// Batched write
db.batch().apply {
    set(docRef1, mapOf("name" to "Alice"))
    update(docRef2, mapOf("age" to 31L))
    delete(docRef3)
}.commit()
```
