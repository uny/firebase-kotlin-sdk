package dev.ynagai.firebase.ai

import dev.ynagai.firebase.Firebase
import dev.ynagai.firebase.FirebaseOptions
import dev.ynagai.firebase.initialize
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FirebaseAIJvmTest {

    @Test
    fun requestsFailWithFirebaseAIException() = runTest {
        val app = Firebase.initialize(
            options = FirebaseOptions(apiKey = "fake-api-key", applicationId = "fake-app-id"),
            name = "jvm-test-ai",
        )
        val model = Firebase.ai(app).generativeModel("gemini-test")

        val error = assertFailsWith<FirebaseAIException> { model.generateContent("hello") }
        assertEquals(FirebaseAIErrorType.INTERNAL, error.errorType)
        assertFailsWith<FirebaseAIException> { model.generateContentStream("hello").collect() }
        assertFailsWith<FirebaseAIException> { model.startChat().sendMessage("hello") }

        app.delete()
    }
}
