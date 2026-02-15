package dev.ynagai.firebase.ai

import com.google.firebase.ai.type.FunctionCallingConfig as AndroidFunctionCallingConfig
import com.google.firebase.ai.type.FunctionDeclaration as AndroidFunctionDeclaration
import com.google.firebase.ai.type.Schema as AndroidSchema
import com.google.firebase.ai.type.Tool as AndroidTool
import com.google.firebase.ai.type.ToolConfig as AndroidToolConfig

internal fun Tool.toAndroid(): AndroidTool =
    AndroidTool.functionDeclarations(
        functionDeclarations?.map { it.toAndroid() } ?: emptyList(),
    )

internal fun FunctionDeclaration.toAndroid(): AndroidFunctionDeclaration {
    val schemaMap = parameters.mapValues { (_, v) -> v.toAndroid() }
    return AndroidFunctionDeclaration(
        name = name,
        description = description,
        parameters = schemaMap,
        optionalParameters = optionalParameters,
    )
}

internal fun Schema.toAndroid(): AndroidSchema = when (type) {
    SchemaType.STRING -> if (enumValues != null) {
        AndroidSchema.enumeration(values = enumValues, description = description, nullable = nullable)
    } else {
        AndroidSchema.string(description = description, nullable = nullable)
    }
    SchemaType.INTEGER -> if (format == "int64") {
        AndroidSchema.long(description = description, nullable = nullable)
    } else {
        AndroidSchema.integer(description = description, nullable = nullable)
    }
    SchemaType.NUMBER -> if (format == "float") {
        AndroidSchema.float(description = description, nullable = nullable)
    } else {
        AndroidSchema.double(description = description, nullable = nullable)
    }
    SchemaType.BOOLEAN -> AndroidSchema.boolean(description = description, nullable = nullable)
    SchemaType.ARRAY -> AndroidSchema.array(
        items = items?.toAndroid() ?: AndroidSchema.string(),
        description = description,
        nullable = nullable,
    )
    SchemaType.OBJECT -> AndroidSchema.obj(
        properties = properties?.mapValues { (_, v) -> v.toAndroid() } ?: emptyMap(),
        optionalProperties = properties?.keys?.filter { it !in (requiredProperties ?: emptyList()) } ?: emptyList(),
        description = description,
        nullable = nullable,
    )
}

internal fun ToolConfig.toAndroid(): AndroidToolConfig = AndroidToolConfig(
    functionCallingConfig = functionCallingConfig?.toAndroid(),
)

internal fun FunctionCallingConfig.toAndroid(): AndroidFunctionCallingConfig = when (mode) {
    FunctionCallingMode.AUTO -> AndroidFunctionCallingConfig.auto()
    FunctionCallingMode.ANY -> if (allowedFunctionNames != null) {
        AndroidFunctionCallingConfig.any(allowedFunctionNames = allowedFunctionNames)
    } else {
        AndroidFunctionCallingConfig.any()
    }
    FunctionCallingMode.NONE -> AndroidFunctionCallingConfig.none()
}
