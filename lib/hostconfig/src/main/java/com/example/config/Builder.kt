package com.example.config

import android.util.Log

class Builder<T : Any>(
    private val inputObject: T,
) {
    private val analysis: AnalysisResult.Available

    init {
        when (val analysis = ConfigLoader.lookupOrAnalyze(inputObject.javaClass)) {
            is AnalysisResult.Available -> {
                this.analysis = analysis
            }

            is AnalysisResult.Unavailable -> {
                error("cannot handle for ${inputObject.javaClass}")
            }
        }
    }

    fun populateWith(config: Map<String, Any>): T {
        Log.d("Builder", "fieldToParser = ${analysis.fieldToParser}")
        return inputObject.apply {
            for ((field, parser) in analysis.fieldToParser) {
                Log.d("Builder", "field = $field, parser = $parser")
                field.access {
                    val parsedValue = parser(inputObject, field, config)
                    field.set(inputObject, parsedValue)
                }
            }
        }
    }

    companion object {
        fun from(configurationMap: Map<String, Any>): UserConfiguration =
            Builder(UserConfiguration())
                .populateWith(configurationMap)
    }
}
