package dev.ynagai.firebase.firestore

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

internal suspend fun <T> Task<T>.awaitWithWrappedExceptions(): T =
    try {
        await()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw e.toCommonFirestoreException()
    }
