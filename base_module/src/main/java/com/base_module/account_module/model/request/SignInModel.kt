package com.base_module.account_module.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignInModel (
    @Expose
    @SerializedName("idToken")
    var idToken: String? = null
)