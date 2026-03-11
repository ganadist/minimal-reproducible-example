package com.example.config

import android.util.Log
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap

internal object ConfigLoader {
    private val valueCache = ConcurrentHashMap<Class<out Any>, AnalysisResult>()

    fun lookupOrAnalyze(clazz: Class<out Any>): AnalysisResult {
        valueCache[clazz]?.let { return it }
        return analyze(clazz).also {
            valueCache.putIfAbsent(clazz, it)
        }
    }

    private fun analyze(clazz: Class<out Any>): AnalysisResult {
        val fieldToParser = mutableMapOf<Field, Parser>()
        for (field in clazz.declaredFields) {
            val keyAnnotation = field.annotationOrNull<ConfigAnnotation.Key>()
            val parser: Parser? =
                when (val analysisResult = keyAnnotation?.validateCustomParser()) {
                    is CustomParserAnalysisResult.Unspecified -> {
                        val parser: (Any?, Any?) -> Any? =
                            KNOWN_PARSERS[field.type]
                                ?: return AnalysisResult.Unavailable(
                                    "${clazz.simpleName}.${field.name} has unknown data type",
                                )
                        Log.d("ConfigLoader", "parser was loaded with known type")
                        KeyFieldParser.WithValueParser(keyAnnotation.name, parser)
                    }

                    is CustomParserAnalysisResult.Specified -> {
                        Log.d("ConfigLoader", "parser was loaded with custom type")
                        KeyFieldParser.WithCustomParser(
                            keyAnnotation.name,
                            analysisResult.customParser,
                        )
                    }

                    else -> {
                        Log.d("ConfigLoader", "parser was failed to load")
                        return AnalysisResult.Unavailable("failed")
                    }
                }
            if (parser != null) {
                fieldToParser[field] = parser
            }
        }
        return AnalysisResult.Available(fieldToParser)
    }

    @Suppress("UNCHECKED_CAST")
    private fun ConfigAnnotation.Key.validateCustomParser(): CustomParserAnalysisResult {
        Log.d("ConfigLoader", "customParser = $customParser")
        val capturedClass = customParser.takeUnless { it == Unspecified::class }
        Log.d("ConfigLoader", "customParserClass = $capturedClass")
        val javaClass: Class<out ConfigParser<Any>> = capturedClass!!.java as Class<out ConfigParser<Any>>
        Log.d("ConfigLoader", "javaClass = $javaClass")

        return try {
            val parser = javaClass.getDeclaredConstructor().newInstance()
            Log.d("ConfigLoader", "parser was loaded: $parser")
            CustomParserAnalysisResult.Specified(parser)
        } catch (ignore: ReflectiveOperationException) {
            Log.d("ConfigLoader", "failed to load parser", ignore)
            CustomParserAnalysisResult.FailedToInitiate(javaClass)
        }
    }
}

internal typealias Parser =
    (receiver: Any, field: Field, config: Map<String, Any>) -> Any?

private typealias ValueParser = (value: Any?, fallback: Any?) -> Any?

internal val KNOWN_PARSERS: Map<Class<out Any>, ValueParser> =
    mapOf(
        Int::class.java to ::parseInt,
        Long::class.java to ::parseLong,
        Boolean::class.java to ::parseBoolean,
        String::class.java to ::parseString,
    )

private fun parseInt(
    value: Any?,
    fallback: Any?,
): Any? {
    val string = value as? String
    return string?.toIntOrNull() ?: fallback
}

private fun parseLong(
    value: Any?,
    fallback: Any?,
): Any? {
    val string = value as? String
    return string?.toLongOrNull() ?: fallback
}

private fun parseBoolean(
    value: Any?,
    fallback: Any?,
): Any? =
    if (value is String) {
        value == "Y"
    } else {
        fallback
    }

@Suppress("UNUSED_PARAMETER")
private fun parseString(
    value: Any?,
    fallback: Any?,
): Any = (value as? String).orEmpty()

internal sealed class KeyFieldParser {
    class WithValueParser(
        val key: String,
        val valueParser: ValueParser,
    ) : KeyFieldParser(),
        Parser {
        override fun invoke(
            receiver: Any,
            field: Field,
            config: Map<String, Any>,
        ): Any? =
            if (key in config) {
                valueParser(config[key], field.get(receiver))
            } else {
                field.get(receiver)
            }
    }

    class WithCustomParser(
        val key: String,
        val customParser: ConfigParser<Any>,
    ) : KeyFieldParser(),
        Parser {
        override fun invoke(
            receiver: Any,
            field: Field,
            config: Map<String, Any>,
        ): Any? =
            if (key in config) {
                customParser.parse(config[key] as? String, field.get(receiver))
            } else {
                field.get(receiver)
            }
    }
}

internal inline fun <T> Field.access(action: (Field) -> T) {
    val accessibilityBackup = isAccessible
    try {
        isAccessible = true
        action(this)
    } finally {
        isAccessible = accessibilityBackup
    }
}

internal inline fun <reified T : Annotation> Field.annotationOrNull(): T? = getAnnotation(T::class.java)

internal sealed class AnalysisResult {
    class Unavailable(
        val reason: String,
    ) : AnalysisResult()

    class Available(
        val fieldToParser: Map<Field, Parser>,
    ) : AnalysisResult()
}

private sealed class CustomParserAnalysisResult {
    abstract val reason: String

    object Unspecified : CustomParserAnalysisResult() {
        override val reason = ""
    }

    class FailedToInitiate(
        val clazz: Class<*>,
    ) : CustomParserAnalysisResult() {
        override val reason = """"${clazz.simpleName}" cannot be instantiated"""
    }

    class Specified(
        val customParser: ConfigParser<Any>,
    ) : CustomParserAnalysisResult() {
        override val reason = ""
    }
}
