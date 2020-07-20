package com.example.myapplication

import okhttp3.OkHttpClient
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.conscrypt.OpenSSLProvider
import java.security.Security

@RunWith(RobolectricTestRunner::class)
@Config(minSdk = 21)
class OkhttpLoadingTest {

  @Test
  fun testOkHttpClientBulid() {
    Security.removeProvider("BC");
    Security.insertProviderAt(OpenSSLProvider(), 1);
    val client: OkHttpClient = OkHttpClient.Builder().build()
  }
}

