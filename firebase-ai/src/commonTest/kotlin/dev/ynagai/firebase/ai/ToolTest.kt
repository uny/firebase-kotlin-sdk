package dev.ynagai.firebase.ai

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ToolTest {

    @Test
    fun functionDeclarationWithParameters() {
        val decl = FunctionDeclaration(
            name = "getWeather",
            description = "Get weather for a city",
            parameters = mapOf(
                "city" to Schema.string(description = "City name"),
                "unit" to Schema.enumeration(values = listOf("celsius", "fahrenheit")),
            ),
            optionalParameters = listOf("unit"),
        )
        assertEquals("getWeather", decl.name)
        assertEquals("Get weather for a city", decl.description)
        assertEquals(2, decl.parameters.size)
        assertEquals(listOf("unit"), decl.optionalParameters)
    }

    @Test
    fun functionDeclarationDefaultParameters() {
        val decl = FunctionDeclaration(
            name = "noParams",
            description = "No parameters",
        )
        assertTrue(decl.parameters.isEmpty())
        assertTrue(decl.optionalParameters.isEmpty())
    }

    @Test
    fun toolFunctionDeclarations() {
        val decl = FunctionDeclaration(name = "fn", description = "A function")
        val tool = Tool.functionDeclarations(listOf(decl))
        assertIs<Tool.FunctionDeclarations>(tool)
        assertEquals(1, (tool as Tool.FunctionDeclarations).declarations.size)
        assertEquals("fn", tool.declarations[0].name)
    }

    @Test
    fun toolGoogleSearch() {
        val tool = Tool.googleSearch()
        assertIs<Tool.GoogleSearch>(tool)
    }

    @Test
    fun toolUrlContext() {
        val tool = Tool.urlContext()
        assertIs<Tool.UrlContext>(tool)
    }

    @Test
    fun toolCodeExecution() {
        val tool = Tool.codeExecution()
        assertIs<Tool.CodeExecution>(tool)
    }

    @Test
    fun toolConfigWithFunctionCallingConfig() {
        val config = ToolConfig(
            functionCallingConfig = FunctionCallingConfig(
                mode = FunctionCallingMode.ANY,
                allowedFunctionNames = listOf("fn1", "fn2"),
            ),
        )
        assertEquals(FunctionCallingMode.ANY, config.functionCallingConfig?.mode)
        assertEquals(listOf("fn1", "fn2"), config.functionCallingConfig?.allowedFunctionNames)
    }

    @Test
    fun functionCallingConfigDefaults() {
        val config = FunctionCallingConfig()
        assertEquals(FunctionCallingMode.AUTO, config.mode)
        assertEquals(null, config.allowedFunctionNames)
    }

    @Test
    fun functionCallingModeValues() {
        val values = FunctionCallingMode.entries
        assertEquals(3, values.size)
        assertTrue(values.contains(FunctionCallingMode.AUTO))
        assertTrue(values.contains(FunctionCallingMode.ANY))
        assertTrue(values.contains(FunctionCallingMode.NONE))
    }

    @Test
    fun dataClassEquality() {
        val a = FunctionDeclaration(name = "fn", description = "desc")
        val b = FunctionDeclaration(name = "fn", description = "desc")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }
}
