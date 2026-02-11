package dev.ynagai.firebase.ai

/**
 * A tool that the model may use to generate a response.
 *
 * @property functionDeclarations List of function declarations available to the model.
 */
data class Tool(
    val functionDeclarations: List<FunctionDeclaration>? = null,
)

/**
 * A declaration of a function that the model can call.
 *
 * @property name The name of the function.
 * @property description A description of what the function does.
 * @property parameters The parameters of the function as a map of parameter name to [Schema].
 * @property optionalParameters List of parameter names that are optional.
 */
data class FunctionDeclaration(
    val name: String,
    val description: String,
    val parameters: Map<String, Schema> = emptyMap(),
    val optionalParameters: List<String> = emptyList(),
)

/**
 * Configuration for how the model should use tools.
 *
 * @property functionCallingConfig Configuration for function calling behavior.
 */
data class ToolConfig(
    val functionCallingConfig: FunctionCallingConfig? = null,
)

/**
 * Configuration for function calling behavior.
 *
 * @property mode The mode of function calling.
 * @property allowedFunctionNames Optional list of function names the model is allowed to call.
 */
data class FunctionCallingConfig(
    val mode: FunctionCallingMode = FunctionCallingMode.AUTO,
    val allowedFunctionNames: List<String>? = null,
)

/**
 * Mode for function calling behavior.
 */
enum class FunctionCallingMode {
    /** The model decides whether to call a function. */
    AUTO,
    /** The model must call one of the provided functions. */
    ANY,
    /** The model will not call any functions. */
    NONE,
}
