package com.base_module.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.hideKeyBoard(view: View?){
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.hideSoftInputFromWindow(view?.windowToken, 0)
}