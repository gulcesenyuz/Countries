package com.example.countries.util

import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.countries.R

fun ImageView.loadUrl(url: String) {

    val imageLoader = ImageLoader.Builder(this.context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()


    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .placeholder(R.drawable.ic_baseline_image_24)
        .error(R.drawable.ic_baseline_error_24)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}