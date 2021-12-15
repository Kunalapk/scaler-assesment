package com.base_module.account_module.screens.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base_module.R
import com.base_module.account_module.viewmodel.AccountViewModel
import com.base_module.base.BaseActivity
import com.base_module.base.BaseFragment
import com.base_module.constants.BaseAppConstants
import com.base_module.databinding.FragmentMobileOtpVerificationBinding
import com.base_module.helpers.LogHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

import androidx.lifecycle.Observer
import com.base_module.account_module.model.request.SignInModel
import com.base_module.constants.ActivityConstants
import com.base_module.constants.SharedPrefConstant
import com.base_module.helpers.SharedPreferencesHelper


class MobileVerificationOtpFragment : BaseFragment() {

    companion object{
        var TAG = BaseFragment::class.java.simpleName

        fun getInstance(verificationId:String,mobileNumber:String):MobileVerificationOtpFragment{
            return MobileVerificationOtpFragment().apply {
                arguments = Bundle().apply {
                    putString(BaseAppConstants.PHONE_AUTH_VERIFICATION_ID,verificationId)
                    putString(BaseAppConstants.KEY_MOBILE_NUMBER,mobileNumber)
                }
            }
        }
    }

    private lateinit var accountViewModel: AccountViewModel
    private var _viewBinder: FragmentMobileOtpVerificationBinding? = null

    private var mobileNumer:String? = null
        get(){
            return arguments?.getString(BaseAppConstants.KEY_MOBILE_NUMBER)
        }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinder = FragmentMobileOtpVerificationBinding.inflate(inflater, container, false)
        return _viewBinder?.root
    }

    override fun initViewModels() {
        accountViewModel = getViewModel(fragment = this,viewModel = AccountViewModel(activity as BaseActivity),className = AccountViewModel::class.java)
    }

    override fun initView(view: View) {
        _viewBinder?.tvVerificationCodeSentTo?.text = getString(R.string.text_verification_code_sent_to,mobileNumer)
    }

    override fun setListeners() {
        setOnClickListener(_viewBinder?.btnVerifyOtp)
    }

    override fun onViewClick(view: View?) {
        when(view?.id){
            R.id.btnVerifyOtp -> doMobileVerification()
        }
    }

    override fun setObservers() {
        accountViewModel.userAccountLiveData.observe(this, Observer {
            if(it.token!=null){
                SharedPreferencesHelper.putString(SharedPrefConstant.ACCESS_TOKEN,it.token)
                SharedPreferencesHelper.putString(SharedPrefConstant.USER_NAME,it.user_id)
                SharedPreferencesHelper.putString(SharedPrefConstant.PROFILE_ICON,it.profile_url)
                goToHomeActivity()
            }
        })
    }


    private fun goToHomeActivity(){
        if(activity!=null){
            startActivity(Intent().setClassName(activity!!, ActivityConstants.VideosAcitivity))
            activity?.finish()
        }
    }

    private fun doMobileVerification(){
        if(!_viewBinder?.otpView?.otp?.toString().isNullOrEmpty()){
            val verificationId:String? = arguments?.getString(BaseAppConstants.PHONE_AUTH_VERIFICATION_ID)
            if(verificationId!=null){
                showBlockingLoader()
                val credential = PhoneAuthProvider.getCredential(verificationId,_viewBinder?.otpView?.otp?.toString()!!)
                signInWithPhoneAuthCredential(credential)
                LogHelper.debug(TAG,"Credentials - $verificationId")
            }
        }

    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                task.result.user?.getIdToken(true)?.addOnCompleteListener {
                    if (task.isSuccessful) {
                        hideBlockingLoader()
                        val idToken: String? = it.result.token
                        if(idToken!=null){
                            doMobileLoginOnServer(idToken)
                        }
                        LogHelper.debug(TAG,"idtoken - "+idToken)
                    }else{
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            hideBlockingLoader()
                            showToast(task.exception?.localizedMessage)
                        }
                    }
                }
            }
    }

    private fun doMobileLoginOnServer(idToken:String){
        accountViewModel.doSignIn(SignInModel(idToken = idToken),true)
    }


}