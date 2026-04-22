package dev.ynagai.firebase.ai

/**
 * Best-effort HTTP status extractor for SDK error messages that don't expose
 * the status as a structured field. Matches only explicit markers
 * (`HTTP 500`, `status: 500`, `code 503`, `(500)`) to avoid false positives
 * from messages that happen to contain a 3-digit number.
 *
 * Returns `null` if no marker is found, or the parsed number is outside
 * the HTTP 4xx/5xx range.
 */
internal fun parseHttpStatusFromMessage(message: String?): Int? {
    if (message.isNullOrEmpty()) return null
    val match = httpStatusMarkerRegex.find(message) ?: return null
    for (i in 1..match.groupValues.lastIndex) {
        val group = match.groupValues[i]
        if (group.isEmpty()) continue
        val code = group.toIntOrNull() ?: return null
        return code.takeIf { it in 400..599 }
    }
    return null
}

private val httpStatusMarkerRegex = Regex(
    """(?i)(?:HTTP[/\s]*(?:\d\.\d\s+)?(\d{3})""" +
        """|status(?:\s*code)?\s*[:=]?\s*(\d{3})""" +
        """|(?:error\s*)?code\s*[:=]?\s*(\d{3})""" +
        """|\((\d{3})\))"""
)
