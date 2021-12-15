package com.base_module.account_module.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code")
    var errorCode: Int=-1,

    @SerializedName("error_description")
    var error_description: String?=null,

)
