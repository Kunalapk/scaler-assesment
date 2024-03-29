package com.base_module.network.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BaseViewModelFactory(private val viewModel: ViewModel,private val className:Class<*>) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(className)){
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}