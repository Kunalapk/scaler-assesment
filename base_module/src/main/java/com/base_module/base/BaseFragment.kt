package com.base_module.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.base_module.account_module.model.ErrorResponse
import com.base_module.helpers.SharedPreferencesHelper
import com.base_module.network.api.BaseViewModelFactory
import java.util.*

abstract class BaseFragment :Fragment(){

    companion object{
        var TAG = BaseFragment::class.java.simpleName
    }

    protected val _activity by lazy { context as BaseActivity }

    abstract fun initViewModels()
    abstract fun onViewClick(view: View?)
    abstract fun initView(view: View)
    abstract fun setListeners()
    abstract fun setObservers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setAppLanguage(SharedPreferencesHelper.getUserLanguage(),false);

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModels()
        initView(view)
        setListeners()
        setObservers()
    }

    protected fun setOnClickListener(view: View?){
        view?.setOnClickListener(onClickListener)
    }

    protected val onClickListener = View.OnClickListener { view ->
        onViewClick(view)
    }

    protected fun showBlockingLoader(){
        if(activity is BaseActivity){
            (activity as BaseActivity).showBlockingLoader()
        }
    }

    protected fun hideBlockingLoader(){
        if(activity is BaseActivity){
            (activity as BaseActivity).hideBlockingLoader()
        }
    }

    protected fun showGenericErrorAlertDialog(errorResponse: ErrorResponse?){
        if(activity is BaseActivity) {
            (activity as BaseActivity).showGenericErrorAlertDialog(errorResponse)
        }
    }

    protected fun showToast(message: String?){
        if(activity is BaseActivity) {
            (activity as BaseActivity).showToast(message)
        }
    }

    protected fun<T:ViewModel> getViewModel(fragment:Fragment,viewModel: ViewModel,className:Class<T>): T {
        return ViewModelProviders.of(fragment, BaseViewModelFactory(viewModel,className)).get(className)
    }

    private fun setAppLanguage(lang: String, recreate: Boolean) {
        try {
            if (recreate) {
                recreate
            }
            val config: Configuration? = context?.resources?.configuration
            val locale = Locale(lang)
            Locale.setDefault(locale)
            config?.setLocale(locale)
            context?.resources?.updateConfiguration(config, context?.resources!!.displayMetrics)
        }catch (e:Exception){

        }
    }


}