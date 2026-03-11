package com.example.config

import org.json.JSONArray
import org.json.JSONException

data class UserConfiguration(
    @ConfigAnnotation.Key("userId", customParser = UserIdParser::class)
    val userId: String = "",
) {
    private class UserIdParser : ConfigParser<String> {
        override fun parse(
            value: String?,
            fallback: String?,
        ): String? {
            println("value = $value")
            if (value.isNullOrEmpty()) {
                return fallback
            }
            val config =
                try {
                    JSONArray(value).getJSONObject(0)
                } catch (_: JSONException) {
                    return fallback
                }
            println("config = $config")
            return config.optString("userId")
        }
    }
}
