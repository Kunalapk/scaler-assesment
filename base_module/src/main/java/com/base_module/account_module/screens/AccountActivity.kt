package com.base_module.account_module.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.base_module.BuildConfig
import com.base_module.R
import com.base_module.account_module.screens.fragments.MobileVerificationOtpFragment
import com.base_module.account_module.viewmodel.AccountViewModel
import com.base_module.base.BaseActivity
import com.base_module.constants.ActivityConstants
import com.base_module.constants.SharedPrefConstant
import com.base_module.databinding.ActivityAccountBinding
import com.base_module.extensions.addFragmentWithFadeIn
import com.base_module.helpers.SharedPreferencesHelper
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class AccountActivity : BaseActivity() {

    private val _viewBinder by lazy { ActivityAccountBinding.inflate(layoutInflater) }

    private var mobileVerificationOtpFragment:MobileVerificationOtpFragment? = null
    private lateinit var accountViewModel: AccountViewModel
    private var mobileNumber:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseAuth.getInstance().signOut()
    }

    override fun initViewModels() {
        accountViewModel = getViewModel(activity = this, viewModel = AccountViewModel(this), className = AccountViewModel::class.java)
    }

    override fun initView() {
        checkTokenAndGetPersonalInfo(false)
        if(BuildConfig.DEBUG){
            _viewBinder.apply {
                etUserMobile.setText(BuildConfig.TEST_MOBILE_NUMBER)
            }
        }

    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            hideBlockingLoader()
            //howToast("Credentials - ${credential.signInMethod}")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            hideBlockingLoader()
            showToast("ErrorException - $e")
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            hideBlockingLoader()
            showOtpVerificationFragment(verificationId)
        }
    }



    override fun setListeners() {
        setOnClickListener(_viewBinder.btnLogin)
    }

    override fun onViewClick(view: View?) {
        when(view?.id){
            R.id.btnLogin -> {
                doLogin()
            }
        }
    }

    override fun setObservers() {

    }

    private fun checkTokenAndGetPersonalInfo(showBlockingLoader:Boolean){
        if(SharedPreferencesHelper.getString(SharedPrefConstant.ACCESS_TOKEN)!=null){
            goToHomeActivity()
        }else{
            setDefaultContentView()
        }
    }

    private fun setDefaultContentView(){
        setContentView(_viewBinder.root)
        _viewBinder.clRoot.animate()?.alpha(1f)?.duration = 500

    }

    private fun goToHomeActivity(){
        startActivity(Intent().setClassName(this, ActivityConstants.VideosAcitivity))
        finish()
    }

    private fun doLogin(){
        if (_viewBinder.etUserMobile.getText().trim().isNullOrBlank()){
            _viewBinder.apply {
                etUserMobile.setError(getString(R.string.error_empty_mobile))
            }
        }else{
            _viewBinder.apply {
                etUserMobile.setError("")
            }
            mobileNumber = _viewBinder.etUserMobile.getText().toString()
            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(_viewBinder.etUserMobile.getText().toString())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build()

            showBlockingLoader()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private fun showOtpVerificationFragment(verificationId:String){
        if(mobileVerificationOtpFragment==null){
            mobileVerificationOtpFragment = MobileVerificationOtpFragment.getInstance(verificationId,mobileNumber)
        }
        if(mobileVerificationOtpFragment?.isAdded==false){
            supportFragmentManager.addFragmentWithFadeIn(R.id.flAccountContainer, mobileVerificationOtpFragment!!, MobileVerificationOtpFragment.TAG)
        }
    }

}