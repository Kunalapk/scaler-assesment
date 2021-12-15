package com.module_social.api.repository

import com.base_module.base.BaseActivity
import com.base_module.model.CommentListModel
import com.base_module.model.CommentModel
import com.base_module.model.VideoListModel
import com.base_module.model.VideoModel
import com.base_module.network.api.BaseRepository
import com.base_module.utils.GSONUtility
import com.module_social.api.VideoApiService

class VideoRepository(private val videoApiService: VideoApiService, private val baseActivity: BaseActivity?) : BaseRepository(baseActivity,videoApiService) {

    suspend fun getVideos(showBlockingLoader:Boolean): VideoListModel? {
        return doSafeAPIRequest(call = { videoApiService.getVideos() }, showBlockingLoader = showBlockingLoader)
    }

    suspend fun markVideoView(postId:String): VideoModel? {
        return doSafeAPIRequest(call = { videoApiService.markVideoView(postId) }, showBlockingLoader = false)
    }

    suspend fun getVideoComments(postId:String): CommentListModel? {
        return doSafeAPIRequest(call = { videoApiService.getVideoComments(postId) }, showBlockingLoader = false)
    }

    suspend fun saveVideoComment(body:String): CommentModel? {
        return doSafeAPIRequest(call = { videoApiService.saveVideoComment(body) }, showBlockingLoader = false)
    }

}