package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSJSONSerialization
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFileDataPart
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFunctionCallPart
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFunctionResponsePart
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBInlineDataPart
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBModelContent
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBTextPart

@OptIn(ExperimentalForeignApi::class)
internal fun Content.toApple(): KFBModelContent {
    val appleParts = parts.map { part ->
        when (part) {
            is TextPart -> KFBTextPart(text = part.text)
            is InlineDataPart -> KFBInlineDataPart(
                data = part.data.toNSData(),
                mimeType = part.mimeType,
            )
            is FileDataPart -> KFBFileDataPart(
                uri = part.uri,
                mimeType = part.mimeType,
            )
            is FunctionCallPart -> KFBFunctionCallPart(
                name = part.name,
                args = part.args as Map<Any?, *>,
            )
            is FunctionResponsePart -> KFBFunctionResponsePart(
                name = part.name,
                response = part.response as Map<Any?, *>,
            )
        }
    }
    return KFBModelContent(role = role ?: "user", parts = appleParts)
}

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
internal fun KFBModelContent.toCommon(): Content = Content(
    role = role(),
    parts = parts()?.mapNotNull { part ->
        when (part) {
            is KFBTextPart -> TextPart(text = part.text())
            is KFBInlineDataPart -> InlineDataPart(
                mimeType = part.mimeType(),
                data = (part.data() as? NSData)?.toByteArray() ?: byteArrayOf(),
            )
            is KFBFileDataPart -> FileDataPart(
                mimeType = part.mimeType(),
                uri = part.uri(),
            )
            is KFBFunctionCallPart -> {
                val args = part.argsData()?.let { data ->
                    NSJSONSerialization.JSONObjectWithData(data, 0u, null) as? Map<String, Any?>
                } ?: emptyMap()
                FunctionCallPart(name = part.name(), args = args)
            }
            is KFBFunctionResponsePart -> {
                val response = part.responseData()?.let { data ->
                    NSJSONSerialization.JSONObjectWithData(data, 0u, null) as? Map<String, Any?>
                } ?: emptyMap()
                FunctionResponsePart(name = part.name(), response = response)
            }
            else -> null
        }
    } ?: emptyList(),
)
