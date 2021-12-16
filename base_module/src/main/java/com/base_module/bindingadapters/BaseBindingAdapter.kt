package com.base_module.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.base_module.R
import com.base_module.extensions.loadImage

object BaseBindingAdapter {

    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImage(view: ImageView?, url: String?) {
        view?.loadImage(url, R.drawable.scaler_placeholder)
    }
}