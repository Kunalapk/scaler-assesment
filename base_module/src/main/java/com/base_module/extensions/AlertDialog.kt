package com.base_module.extensions

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.base_module.R

fun AlertDialog.addDialogTheme() {
    this.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.theme_color))
    this.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.dialog_negative_color))
}