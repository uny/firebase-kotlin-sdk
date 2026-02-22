package dev.ynagai.firebase.firestore.integration

import androidx.test.platform.app.InstrumentationRegistry
import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseOptions
import dev.ynagai.firebase.firestore.FirebaseFirestore
import dev.ynagai.firebase.firestore.firestore
import dev.ynagai.firebase.initialize
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class FirestoreEmulatorTest {
    protected lateinit var firestore: FirebaseFirestore

    @BeforeTest
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val options = FirebaseOptions(
            apiKey = "fake-api-key",
            applicationId = "1:123456789:android:abcdef",
            projectId = "firebase-kotlin-sdk-test",
        )
        val app = Firebase.initialize(
            context = context,
            options = options,
            name = "test-${System.nanoTime()}",
        )
        firestore = Firebase.firestore(app)
        firestore.useEmulator("10.0.2.2", 8080)
    }

    @AfterTest
    fun tearDown() = runTest {
        firestore.terminate()
    }
}
