package com.example.mylib

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy

class GlideRequestFactory {
    fun createRequest(requests: RequestManager): RequestBuilder<Drawable> =
        requests.load("http://via.placeholder.com/300.png")
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .circleCrop()
}
