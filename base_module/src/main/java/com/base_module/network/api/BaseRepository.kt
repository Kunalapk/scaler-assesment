package com.base_module.network.api

import com.base_module.base.BaseActivity
import com.base_module.helpers.LogHelper
import com.base_module.utils.GSONUtility
import retrofit2.Response
import com.base_module.utils.Result

open class BaseRepository(private val baseActivity: BaseActivity?, private val baseApiService: BaseApiService) {

    suspend fun <T : Any> doSafeAPIRequest(call: suspend () -> Response<T>,showBlockingLoader:Boolean): T? {
        val result : Result<T> = returnSafeAPIResponse(call,baseActivity,showBlockingLoader)
        var data : T? = null

        when(result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                throw Exception(result.error ?: "")
            }
        }
        return data
    }

    suspend fun <T : Any> doSafeAPIRequest(call: suspend () -> Response<T>): T? {
        return doSafeAPIRequest(call,false)
    }

    private suspend fun <T: Any> returnSafeAPIResponse(call: suspend ()-> Response<T>,baseActivity: BaseActivity?,showBlockingLoader:Boolean) : Result<T> {
        if(showBlockingLoader){
            baseActivity?.showBlockingLoader()
        }

        try{
            val response = call.invoke()
            if(response.isSuccessful) {
                baseActivity?.hideBlockingLoader()
                return Result.Success(response.body()!!)
            }

            baseActivity?.hideBlockingLoader()

            return Result.Error(error = response.errorBody()?.string().toString())
        }catch (e:Exception){
            baseActivity?.hideBlockingLoader()
            return Result.Error("Something went wrong!")
        }
    }

}
