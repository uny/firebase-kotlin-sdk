package dev.ynagai.firebase

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FirebaseAppJvmTest {

    private val options = FirebaseOptions(apiKey = "fake-api-key", applicationId = "fake-app-id")

    @Test
    fun appIsAvailableAfterInitializeAndGoneAfterDelete() = runTest {
        val name = "jvm-test-lifecycle"
        assertFailsWith<IllegalStateException> { Firebase.app(name) }

        val app = Firebase.initialize(options = options, name = name)
        assertEquals(name, Firebase.app(name).name)
        assertEquals(options, Firebase.app(name).options)
        assertTrue(Firebase.apps().any { it.name == name })

        app.delete()
        assertFailsWith<IllegalStateException> { Firebase.app(name) }
    }

    @Test
    fun initializeTwiceWithSameNameFails() = runTest {
        val name = "jvm-test-duplicate"
        val app = Firebase.initialize(options = options, name = name)
        assertFailsWith<IllegalStateException> { Firebase.initialize(options = options, name = name) }
        app.delete()
    }
}
