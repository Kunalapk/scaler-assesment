package com.base_module.account_module.api

import com.base_module.account_module.model.response.LoginResponse
import com.base_module.constants.BaseAPIConstants
import com.base_module.network.api.BaseApiService
import retrofit2.Response
import retrofit2.http.*


interface AccountApiService: BaseApiService {

    @Headers(BaseAPIConstants.CONTENT_TYPE_JSON)
    @POST(BaseAPIConstants.API_LOGIN)
    suspend fun doPhoneLogin(@Body body: String): Response<LoginResponse>

}