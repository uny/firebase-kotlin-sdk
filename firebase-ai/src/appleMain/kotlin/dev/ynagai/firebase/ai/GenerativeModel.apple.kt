package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.Foundation.NSError
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBCountTokensResponse
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBGenerateContentResponse
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBGenerativeModel

@OptIn(ExperimentalForeignApi::class)
actual class GenerativeModel internal constructor(
    internal val apple: KFBGenerativeModel,
) {
    actual suspend fun generateContent(vararg content: Content): GenerateContentResponse {
        val appleContents = content.map { it.toApple() }
        val result = awaitResult<KFBGenerateContentResponse> { callback ->
            apple.generateContent(appleContents, completionHandler = callback)
        }
        return result.toCommon()
    }

    actual suspend fun generateContent(prompt: String): GenerateContentResponse {
        val result = awaitResult<KFBGenerateContentResponse> { callback ->
            apple.generateContentFromText(prompt, completionHandler = callback)
        }
        return result.toCommon()
    }

    actual fun generateContentStream(vararg content: Content): Flow<GenerateContentResponse> = flow {
        val appleContents = content.map { it.toApple() }
        val stream = memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            val result = apple.generateContentStream(appleContents, error = errorPtr.ptr)
            errorPtr.value?.let { throw it.toFirebaseAIException() }
            result ?: throw FirebaseAIException("Failed to create content stream")
        }
        while (true) {
            val response = awaitNullableResult<KFBGenerateContentResponse> { callback ->
                stream.nextWithCompletionHandler(callback)
            } ?: break
            emit(response.toCommon())
        }
    }

    actual fun generateContentStream(prompt: String): Flow<GenerateContentResponse> = flow {
        val stream = memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            val result = apple.generateContentStreamFromText(prompt, error = errorPtr.ptr)
            errorPtr.value?.let { throw it.toFirebaseAIException() }
            result ?: throw FirebaseAIException("Failed to create content stream")
        }
        while (true) {
            val response = awaitNullableResult<KFBGenerateContentResponse> { callback ->
                stream.nextWithCompletionHandler(callback)
            } ?: break
            emit(response.toCommon())
        }
    }

    actual suspend fun countTokens(vararg content: Content): CountTokensResponse {
        val appleContents = content.map { it.toApple() }
        val result = awaitResult<KFBCountTokensResponse> { callback ->
            apple.countTokens(appleContents, completionHandler = callback)
        }
        return CountTokensResponse(totalTokens = result.totalTokens().toInt())
    }

    actual suspend fun countTokens(prompt: String): CountTokensResponse {
        val result = awaitResult<KFBCountTokensResponse> { callback ->
            apple.countTokensFromText(prompt, completionHandler = callback)
        }
        return CountTokensResponse(totalTokens = result.totalTokens().toInt())
    }

    actual fun startChat(history: List<Content>): Chat =
        Chat(apple.startChatWithHistory(history.map { it.toApple() }))
}
