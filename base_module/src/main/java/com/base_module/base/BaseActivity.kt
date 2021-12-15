package com.base_module.base

import android.Manifest
import android.content.*
import android.content.pm.ActivityInfo
import android.content.res.AssetManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.PermissionChecker
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.base_module.BuildConfig
import com.base_module.R
import com.base_module.account_module.model.ErrorResponse
import com.base_module.constants.SharedPrefConstant
import com.base_module.extensions.addDialogTheme
import com.base_module.helpers.SharedPreferencesHelper
import com.base_module.network.api.BaseViewModelFactory
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

abstract class BaseActivity :AppCompatActivity(){

    companion object{
        var TAG:String = BaseActivity::class.java.simpleName
    }

    private var toastMessage: Toast? = null

    private var loaderAlertDialog:AlertDialog? = null


    abstract fun initViewModels()
    abstract fun initView()
    abstract fun setObservers()
    abstract fun setListeners()
    abstract fun onViewClick(view: View?)

    private var permsList = arrayOf<String>(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CALL_PHONE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        if (SharedPreferencesHelper.getBoolean(SharedPrefConstant.DARK_MODE)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        addOverrideAnimationOnStart()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permsList += arrayOf<String>(Manifest.permission.ANSWER_PHONE_CALLS)
        }



        setAppLanguage(SharedPreferencesHelper.getUserLanguage(),false);

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        initViewModels()
        initView()
        setObservers()
        setListeners()

    }

    fun setFullScreen(){
        if(!hasNavBar()){
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    private fun hasNavBar(): Boolean {
        val id: Int = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        return if (id > 0) {
            resources.getBoolean(id)
        } else {
            val hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey()
            val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            !hasMenuKey && !hasBackKey
        }
    }

    override fun getAssets(): AssetManager {
        return resources.assets
    }


    fun setOnClickListener(view: View?){
        view?.setOnClickListener(onClickListener)
    }

    fun hasPermission(permission: String) =
        PermissionChecker.checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED


    private val onClickListener = View.OnClickListener { view ->
        onViewClick(view)
    }

    fun showBlockingLoader(){
        runOnUiThread {
            if(loaderAlertDialog==null){
                initBlockingLoaderDialog()
            }
            loaderAlertDialog?.show()
        }
    }

    fun hideBlockingLoader(){
        runOnUiThread {
            if(loaderAlertDialog?.isShowing==true){
                loaderAlertDialog?.dismiss()
            }
        }
    }

    fun showToast(message:String?){
        if (message != null) {
            cancelToastMessage()
            if (applicationContext != null) {
                toastMessage = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
                toastMessage?.show()
            }
        }
    }

    override fun onDestroy() {
        loaderAlertDialog?.dismiss()
        loaderAlertDialog = null
        super.onDestroy()
    }

    fun showGenericErrorAlertDialog(errorResponse: ErrorResponse?){
        //LogHelper.debug(TAG,"ErrorMessage - ${errorResponse?.errors?.__all__?.size}")
        if(errorResponse?.error_description!=null){
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage(errorResponse.error_description ?: getString(R.string.something_went_wrong_pls_try_again))
            alertDialog.setPositiveButton(getString(R.string.okay), DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            alertDialog.show().addDialogTheme()
        }
    }

    public fun subscribeToFirebase(){
        try {
            FirebaseMessaging.getInstance().subscribeToTopic("admin")
            if(BuildConfig.DEBUG){
                FirebaseMessaging.getInstance().subscribeToTopic("debug")
            }
        }catch (e: Exception){

        }
    }

    private fun cancelToastMessage() {
        toastMessage?.cancel()
    }

    private fun initBlockingLoaderDialog(){
        loaderAlertDialog = AlertDialog.Builder(this@BaseActivity).apply {
            setView(LayoutInflater.from(context).inflate(R.layout.layout_blocking_loader, null))
            setCancelable(true)
        }.create()

        loaderAlertDialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    protected fun<T: ViewModel> getViewModel(activity: AppCompatActivity, viewModel: ViewModel, className:Class<T>): T {
        return ViewModelProvider(activity, BaseViewModelFactory(viewModel,className)).get(className)
    }

    private fun setAppLanguage(lang: String, recreate: Boolean) {
        try {
            if (recreate) {
                recreate()
            }
            val config: Configuration = baseContext.resources.configuration
            val locale = Locale(lang)
            Locale.setDefault(locale)
            config.setLocale(locale)
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }catch (e:Exception){

        }
    }

    override fun finish() {
        super.finish()
        addOverrideAnimationFinish()
    }


    private fun addOverrideAnimationFinish(){
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    private fun addOverrideAnimationOnStart(){
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }



}