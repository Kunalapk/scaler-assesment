package com.base_module.account_module.viewmodel

import androidx.lifecycle.MutableLiveData
import com.base_module.account_module.api.AccountApiService
import com.base_module.account_module.api.repository.AccountRepository
import com.base_module.account_module.model.request.SignInModel
import com.base_module.account_module.model.response.LoginResponse
import com.base_module.base.BaseActivity
import com.base_module.base.BaseViewModel
import com.base_module.network.BaseCloudAPIService
import kotlinx.coroutines.launch

class AccountViewModel(baseActivity: BaseActivity?) : BaseViewModel() {

    private val accountRepository = AccountRepository(BaseCloudAPIService.getApiService(AccountApiService::class.java),baseActivity)

    internal val userAccountLiveData = MutableLiveData<LoginResponse>()

    fun doSignIn(signInModel: SignInModel, showBlockingLoader:Boolean){
        scope.launch {
            try {
                userAccountLiveData.postValue(accountRepository.doSignIn(signInModel,showBlockingLoader))
            } catch (e: Throwable){
                //errorResponseLiveData.postValue(parseErrorResponseFromJson(e.message))
            }
        }
    }

}