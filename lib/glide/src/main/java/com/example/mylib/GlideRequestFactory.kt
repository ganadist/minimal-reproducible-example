package com.example.mylib

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.engine.DiskCacheStrategy

class GlideRequestFactory {
    fun createRequest(requests: GlideRequests): GlideRequest<Drawable> =
        requests.load("http://via.placeholder.com/300.png")
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .circleCrop()
}
