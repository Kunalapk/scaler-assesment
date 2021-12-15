package com.base_module.helpers

import android.content.SharedPreferences
import com.base_module.AppController
import com.base_module.constants.SharedPrefConstant

object SharedPreferencesHelper {


    fun registerSharedPreferenceChangeListener(onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener){
        SharedPreferencesManager.with(context = AppController.appController).registerSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    fun unregisterSharedPreferenceChangeListener(onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener){
        SharedPreferencesManager.with(context = AppController.appController).unregisterSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    fun getStringWithOutNull(key:String):String{
        return SharedPreferencesManager.with(context = AppController.appController).getString(key, null) ?: ""
    }

    fun getString(key:String):String?{
        return SharedPreferencesManager.with(context = AppController.appController).getString(key, null)
    }

    fun getStringWithDefValue(key:String, defValue:String):String?{
        return SharedPreferencesManager.with(context = AppController.appController).getString(key, defValue)
    }

    fun getUserLanguage():String{
        return SharedPreferencesManager.with(context = AppController.appController).getString(SharedPrefConstant.USER_LANGUAGE, SharedPrefConstant.USER_DEFAULT_LANGUAGE) ?: SharedPrefConstant.USER_DEFAULT_LANGUAGE
    }

    fun setUserLanguage(lang:String){
        SharedPreferencesManager.with(context = AppController.appController).edit().apply{
            putString(SharedPrefConstant.USER_LANGUAGE, lang)
            //putBoolean(SharedPrefConstant.IS_LANGUAGE_SELECTED, true)
        }.apply()
    }

    fun isLanguageSelected():Boolean{
        return SharedPreferencesManager.with(context = AppController.appController).getBoolean(SharedPrefConstant.IS_LANGUAGE_SELECTED,false)
    }

    fun setLanguageSelected(boolean: Boolean){
        SharedPreferencesManager.with(context = AppController.appController).edit().putBoolean(SharedPrefConstant.IS_LANGUAGE_SELECTED, boolean).apply()
    }

    fun putString(key: String, value:String?){
        SharedPreferencesManager.with(context = AppController.appController).edit().putString(key, value).apply()
    }

    fun putLong(key: String, value:Long){
        SharedPreferencesManager.with(context = AppController.appController).edit().putLong(key, value).apply()
    }

    fun getBoolean(key:String):Boolean{
        return getBoolean(key,false)
    }

    fun getBoolean(key:String,defValue: Boolean):Boolean{
        return SharedPreferencesManager.with(context = AppController.appController).getBoolean(key, defValue)
    }

    fun putBoolean(key: String, boolean:Boolean){
        SharedPreferencesManager.with(context = AppController.appController).edit().putBoolean(key, boolean).apply()
    }

    fun getInt(key:String):Int{
        return SharedPreferencesManager.with(context = AppController.appController).getInt(key, 1)
    }

    fun getFloat(key:String):Float{
        return SharedPreferencesManager.with(context = AppController.appController).getFloat(key, 1.0F)
    }

    fun getLong(key:String):Long{
        return SharedPreferencesManager.with(context = AppController.appController).getLong(key)
    }

    fun getLong(key:String,defValue: Long):Long{
        return SharedPreferencesManager.with(context = AppController.appController).getLong(key,defValue)
    }

    fun getFloat(key:String, def:Float):Float{
        return SharedPreferencesManager.with(context = AppController.appController).getFloat(key, def)
    }

    fun putInt(key: String, value:Int){
        SharedPreferencesManager.with(context = AppController.appController).edit().putInt(key, value).apply()
    }

    fun clearLocalInfo(){
        //val language = getString(SharedPrefConstant.USER_LANGUAGE)
        //val isLanguageSelected = isLanguageSelected()
        SharedPreferencesManager.with(context = AppController.appController).edit().apply {
            clear()
            //putString(SharedPrefConstant.USER_LANGUAGE,language)
            //setLanguageSelected(isLanguageSelected)
        }.apply()
    }

    fun putFloat(key: String, value:Float){
        SharedPreferencesManager.with(context = AppController.appController).edit().putFloat(key, value).apply()
    }

    fun clearAllSharePreferences(){
        SharedPreferencesManager.with(context = AppController.appController).edit().clear().apply()
    }
}