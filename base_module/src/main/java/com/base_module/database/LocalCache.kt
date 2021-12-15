package com.base_module.database

import androidx.lifecycle.LiveData
import com.base_module.database.dao.AppDao
import com.base_module.model.VideoModel
import java.util.concurrent.Executor

class LocalCache(private val appDao: AppDao?, private val ioExecutor: Executor) {

    fun saveVideoList(videos:ArrayList<VideoModel>){
        ioExecutor.execute {
            appDao?.insertVideos(videos)
        }
    }

    fun deleteAllVideos(){
        ioExecutor.execute {
            appDao?.deleteAllVideos()
        }
    }

    fun subscribeToVideoList():LiveData<MutableList<VideoModel>>?{
        return appDao?.getVideosList()
    }

    fun updateVideoViewsCount(videoModel: VideoModel){
        appDao?.updateVideoViewsCount(videoModel.post_views_count,videoModel.post_id)
    }

    fun subscribeToOtherVideoList(videoId:String):LiveData<MutableList<VideoModel>>?{
        return appDao?.getVideosList(videoId)
    }
}