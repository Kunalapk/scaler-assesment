package com.base_module.utils

import android.content.Intent
import com.base_module.AppController
import com.base_module.account_module.screens.AccountActivity
import com.base_module.constants.BaseAppConstants
import com.base_module.helpers.SharedPreferencesHelper


object AppUtility {

    fun callAccountActivityOnLogOut(){
        SharedPreferencesHelper.clearLocalInfo()
        AppController.appController?.applicationContext?.startActivity(
            Intent(AppController.appController?.applicationContext,AccountActivity::class.java)
                .putExtra(BaseAppConstants.KEY_FROM,BaseAppConstants.KEY_FROM_LOGOUT)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }
}