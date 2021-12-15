package com.base_module

import android.app.Application
import android.content.res.Configuration
import com.base_module.database.BaseAppDatabase
import com.base_module.database.LocalCache
import com.base_module.helpers.LogHelper
import com.base_module.network.BaseCloudAPIService
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.firebase.analytics.FirebaseAnalytics
import retrofit2.Retrofit
import java.util.concurrent.Executors

class AppController:Application() {

    companion object{
        val TAG:String = AppController::class.java.simpleName
        var appController:AppController? = null
        var cloudApiService:Retrofit? = null
        var firebaseAnalytics:FirebaseAnalytics? = null
        var localCache: LocalCache? = null
        lateinit var simpleCache: SimpleCache

    }

    override fun onCreate() {
        super.onCreate()

        appController = this

        localCache = LocalCache(appDao = BaseAppDatabase.getInstance(this).appDao(),ioExecutor = Executors.newSingleThreadExecutor())

        cloudApiService = BaseCloudAPIService()

        simpleCache = SimpleCache(cacheDir, LeastRecentlyUsedCacheEvictor(200 * 1024 * 1024),  ExoDatabaseProvider(this))

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogHelper.debug(TAG, "onConfigurationChanged")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        LogHelper.debug(TAG, "onLowMemory")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        LogHelper.debug(TAG, "onTrimMemory")
    }

    override fun onTerminate() {
        LogHelper.debug(TAG, "onTerminate")
        super.onTerminate()
    }

}