package dev.ynagai.firebase.ai

/**
 * A tool that the model may use to generate a response.
 *
 * Use the companion factory methods to create instances.
 */
sealed class Tool {
    /**
     * A tool with function declarations the model may call.
     *
     * @property declarations List of function declarations available to the model.
     */
    data class FunctionDeclarations(
        val declarations: List<FunctionDeclaration>,
    ) : Tool()

    /** A tool that grounds the model's response with Google Search results. */
    data object GoogleSearch : Tool()

    /** A tool that provides URL context to the model for referenced URLs. */
    data object UrlContext : Tool()

    /** A tool that enables code execution by the model. */
    data object CodeExecution : Tool()

    companion object {
        /** Creates a tool with the given function declarations. */
        fun functionDeclarations(declarations: List<FunctionDeclaration>): Tool =
            FunctionDeclarations(declarations)

        /** Creates a tool that grounds the model with Google Search results. */
        fun googleSearch(): Tool = GoogleSearch

        /** Creates a tool that provides URL context to the model. */
        fun urlContext(): Tool = UrlContext

        /** Creates a tool that enables code execution by the model. */
        fun codeExecution(): Tool = CodeExecution
    }
}

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
