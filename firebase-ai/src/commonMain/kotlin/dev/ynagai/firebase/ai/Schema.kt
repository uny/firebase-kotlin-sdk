package dev.ynagai.firebase.ai

/**
 * Defines the structure of input/output data for function calling and structured output.
 *
 * @property type The data type.
 * @property description A description of what the parameter represents.
 * @property format Optional format hint (e.g., "int32", "float", "double", "enum").
 * @property nullable Whether the value can be null.
 * @property enumValues Possible values for an enum-typed schema.
 * @property items Schema for array elements.
 * @property properties Schemas for object properties.
 * @property requiredProperties List of required property names for object schemas.
 */
data class Schema(
    val type: SchemaType,
    val description: String? = null,
    val format: String? = null,
    val nullable: Boolean = false,
    val enumValues: List<String>? = null,
    val items: Schema? = null,
    val properties: Map<String, Schema>? = null,
    val requiredProperties: List<String>? = null,
) {
    companion object {
        fun string(description: String? = null, nullable: Boolean = false): Schema =
            Schema(type = SchemaType.STRING, description = description, nullable = nullable)

        fun integer(description: String? = null, nullable: Boolean = false): Schema =
            Schema(type = SchemaType.INTEGER, description = description, format = "int32", nullable = nullable)

        fun long(description: String? = null, nullable: Boolean = false): Schema =
            Schema(type = SchemaType.INTEGER, description = description, format = "int64", nullable = nullable)

        fun float(description: String? = null, nullable: Boolean = false): Schema =
            Schema(type = SchemaType.NUMBER, description = description, format = "float", nullable = nullable)

        fun double(description: String? = null, nullable: Boolean = false): Schema =
            Schema(type = SchemaType.NUMBER, description = description, format = "double", nullable = nullable)

        fun boolean(description: String? = null, nullable: Boolean = false): Schema =
            Schema(type = SchemaType.BOOLEAN, description = description, nullable = nullable)

        fun array(items: Schema, description: String? = null, nullable: Boolean = false): Schema =
            Schema(type = SchemaType.ARRAY, description = description, items = items, nullable = nullable)

        fun obj(
            properties: Map<String, Schema>,
            requiredProperties: List<String>? = null,
            description: String? = null,
            nullable: Boolean = false,
        ): Schema = Schema(
            type = SchemaType.OBJECT,
            description = description,
            properties = properties,
            requiredProperties = requiredProperties,
            nullable = nullable,
        )

        fun enumeration(
            values: List<String>,
            description: String? = null,
            nullable: Boolean = false,
        ): Schema = Schema(
            type = SchemaType.STRING,
            description = description,
            format = "enum",
            enumValues = values,
            nullable = nullable,
        )
    }
}
