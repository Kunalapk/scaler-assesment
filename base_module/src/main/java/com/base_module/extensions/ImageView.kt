package com.base_module.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener

fun ImageView.loadImage(url: String?, placeholder:Int) {
    if (url != null) {
        Glide.with(context)
            .load(url)
            .dontAnimate()
            .placeholder(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}

fun ImageView.loadImage(url: String?) {
    if (url != null) {
        Glide.with(context)
            .load(url)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}

fun ImageView.loadImage(url: String?,onRequestListener:RequestListener<Drawable>) {
    if (url != null) {
        Glide.with(context)
            .load(url)
            .dontAnimate()
            .listener(onRequestListener)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}

