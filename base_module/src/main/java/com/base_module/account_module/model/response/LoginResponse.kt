package com.base_module.account_module.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginResponse (

    @Expose
    @SerializedName("token")
    var token: String? = null,

    @Expose
    @SerializedName("user_id")
    var user_id: String? = null,

    @Expose
    @SerializedName("user_name")
    var user_name: String? = null,

    @Expose
    @SerializedName("profile_url")
    var profile_url: String? = null,

):Serializable