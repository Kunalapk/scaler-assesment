package com.base_module.utils

import com.google.gson.GsonBuilder

object GSONUtility {

    fun getJSON(model :Any):String{
        return GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create().toJson(model).toString()
    }

}