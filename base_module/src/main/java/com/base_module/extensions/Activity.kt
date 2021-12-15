package com.base_module.extensions

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.base_module.R

fun Activity.hideShowActionBar(show:Boolean) {
    if(this is AppCompatActivity){
        if(show) this.supportActionBar?.show() else this.supportActionBar?.hide()
    }
}