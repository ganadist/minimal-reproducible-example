package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit: Retrofit = Retrofit.Builder()
            .callFactory {
                object :Call {
                    override fun clone(): Call {
                        TODO("Not yet implemented")
                    }

                    override fun request(): Request {
                        TODO("Not yet implemented")
                    }

                    override fun execute(): Response {
                        TODO("Not yet implemented")
                    }

                    override fun enqueue(responseCallback: Callback) {
                        TODO("Not yet implemented")
                    }

                    override fun cancel() {
                        TODO("Not yet implemented")
                    }

                    override fun isExecuted(): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun isCanceled(): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun timeout(): Timeout {
                        TODO("Not yet implemented")
                    }
                }
            }
            .baseUrl("https://google.com")
            .build()
        val converter = MoshiConverterFactory.create(JsonConverter().create())
            .responseBodyConverter(Shape::class.java, arrayOf<Annotation>(), retrofit)

        val jsonString = resources.getString(R.string.json_value)
        val result = converter!!.convert(ResponseBody.create(MediaType.get("text/json"), jsonString))
        Log.e("MainActivity", result.toString())
    }
}