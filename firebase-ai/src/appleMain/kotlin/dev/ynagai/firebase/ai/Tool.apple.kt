package dev.ynagai.firebase.ai

import kotlinx.cinterop.ExperimentalForeignApi
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFunctionCallingConfig
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBFunctionDeclaration
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBSchema
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBTool
import swiftPMImport.dev.ynagai.firebase.firebase.ai.KFBToolConfig

@OptIn(ExperimentalForeignApi::class)
internal fun Tool.toApple(): KFBTool =
    KFBTool.functionDeclarations(
        functionDeclarations?.map { it.toApple() } ?: emptyList<KFBFunctionDeclaration>(),
    )

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
internal fun FunctionDeclaration.toApple(): KFBFunctionDeclaration {
    val appleParams: Map<Any?, *> = parameters.mapValues { (_, v) -> v.toApple() }
    return KFBFunctionDeclaration(
        name = name,
        description = description,
        parameters = appleParams,
        optionalParameters = optionalParameters,
    )
}

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNCHECKED_CAST")
internal fun Schema.toApple(): KFBSchema = when (type) {
    SchemaType.STRING -> if (enumValues != null) {
        KFBSchema.enumerationWithValues(
            values = enumValues,
            description = description,
            title = null,
            nullable = nullable,
        )
    } else {
        KFBSchema.stringWithDescription(
            description = description,
            title = null,
            nullable = nullable,
            format = format,
        )
    }
    SchemaType.INTEGER -> KFBSchema.integerWithDescription(
        description = description,
        title = null,
        nullable = nullable,
        format = format,
        minimum = null,
        maximum = null,
    )
    SchemaType.NUMBER -> if (format == "float") {
        KFBSchema.floatWithDescription(
            description = description,
            title = null,
            nullable = nullable,
            minimum = null,
            maximum = null,
        )
    } else {
        KFBSchema.doubleWithDescription(
            description = description,
            title = null,
            nullable = nullable,
            minimum = null,
            maximum = null,
        )
    }
    SchemaType.BOOLEAN -> KFBSchema.booleanWithDescription(
        description = description,
        title = null,
        nullable = nullable,
    )
    SchemaType.ARRAY -> KFBSchema.arrayWithItems(
        items = items?.toApple() ?: KFBSchema.stringWithDescription(
            description = null,
            title = null,
            nullable = false,
            format = null,
        ),
        description = description,
        title = null,
        nullable = nullable,
        minItems = null,
        maxItems = null,
    )
    SchemaType.OBJECT -> {
        val appleProps: Map<Any?, *> =
            properties?.mapValues { (_, v) -> v.toApple() } ?: emptyMap<Any?, Any?>()
        val optionalProps: List<Any?> = properties?.keys?.filter {
            it !in (requiredProperties ?: emptyList())
        } ?: emptyList<Any?>()
        KFBSchema.objectWithProperties(
            properties = appleProps,
            optionalProperties = optionalProps,
            propertyOrdering = null,
            description = description,
            title = null,
            nullable = nullable,
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
internal fun ToolConfig.toApple(): KFBToolConfig = KFBToolConfig(
    functionCallingConfig = functionCallingConfig?.toApple(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun FunctionCallingConfig.toApple(): KFBFunctionCallingConfig = when (mode) {
    FunctionCallingMode.AUTO -> KFBFunctionCallingConfig.auto()
    FunctionCallingMode.ANY -> KFBFunctionCallingConfig.anyWithAllowedFunctionNames(allowedFunctionNames)
    FunctionCallingMode.NONE -> KFBFunctionCallingConfig.none()
}
