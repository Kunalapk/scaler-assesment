package com.base_module.account_module.api.repository

import com.base_module.account_module.api.AccountApiService
import com.base_module.account_module.model.request.SignInModel
import com.base_module.account_module.model.response.LoginResponse
import com.base_module.base.BaseActivity
import com.base_module.network.api.BaseRepository
import com.base_module.utils.GSONUtility

class AccountRepository(private val accountApiService: AccountApiService, private val baseActivity: BaseActivity?) : BaseRepository(baseActivity,accountApiService) {

    suspend fun doSignIn(signInModel: SignInModel, showBlockingLoader:Boolean): LoginResponse? {
        return doSafeAPIRequest(call = { accountApiService.doPhoneLogin(GSONUtility.getJSON(signInModel)) }, showBlockingLoader = showBlockingLoader)
    }

}