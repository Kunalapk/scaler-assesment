package com.base_module.network

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.base_module.AppController
import com.base_module.BuildConfig
import com.base_module.constants.BaseAPIConstants
import com.base_module.constants.SharedPrefConstant
import com.base_module.helpers.SharedPreferencesHelper
import com.base_module.utils.AppUtility
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit



interface BaseCloudAPIService {

    companion object{
        operator fun invoke(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(OkHttpClient.Builder()
                    .addInterceptor(Interceptor {
                        it.proceed(it.request().newBuilder()
                            .addHeader(BaseAPIConstants.KEY_AUTHORIZATION, "Bearer ${SharedPreferencesHelper.getString(SharedPrefConstant.ACCESS_TOKEN)}")
                            .build())
                    })
                    .addInterceptor(Interceptor {
                        //LogHelper.debug("TAG","CloudService** - ${}")
                        it.proceed(it.request()).let { response ->
                            if((response.code==401) &&
                                !it.request().url.toString().contains(BaseAPIConstants.API_LOGIN)){
                                AppUtility.callAccountActivityOnLogOut()
                            }
                            response
                        }
                    })
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }

        fun<T> getApiService(service: Class<T>):T{
            if(AppController.cloudApiService!=null){
                return AppController.cloudApiService!!.create(service)
            }else{
                throw Throwable("CloudApiService cannot be null")
            }
        }

    }

}