package util

import tools.jackson.core.JsonParser
import tools.jackson.databind.DatabindException
import tools.jackson.databind.ObjectWriter
import tools.jackson.module.kotlin.jacksonObjectMapper
import tools.jackson.module.kotlin.jacksonTypeRef
import java.io.File

object JsonHelper {
    val objectMapper = jacksonObjectMapper()

    inline fun <reified T> readValue(src: File): T = objectMapper.readValue(src, jacksonTypeRef<T>()).checkTypeMismatch()

    inline fun <reified T> Any?.checkTypeMismatch(): T {
        // Basically, this check assumes that T is non-null and the value is null.
        // Since this can be caused by both input or ObjectMapper implementation errors,
        // a more abstract DatabindException is thrown.
        if (this !is T) {
            val nullability = if (null is T) "?" else "(non-null)"

            throw DatabindException.from(
                null as JsonParser?,
                "Deserialized value did not match the specified type; " +
                        "specified ${T::class.qualifiedName}${nullability} but was ${this?.let { it::class.qualifiedName }}"
            )
        }
        return this
    }

    fun writerWithDefaultPrettyPrinter(): ObjectWriter {
        return objectMapper.writerWithDefaultPrettyPrinter()
    }
}