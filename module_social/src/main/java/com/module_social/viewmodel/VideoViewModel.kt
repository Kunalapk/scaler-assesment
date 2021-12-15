package com.module_social.viewmodel

import androidx.lifecycle.MutableLiveData
import com.base_module.AppController.Companion.localCache
import com.base_module.base.BaseActivity
import com.base_module.base.BaseViewModel
import com.base_module.model.CommentListModel
import com.base_module.model.CommentModel
import com.base_module.model.VideoListModel
import com.base_module.model.VideoModel
import com.base_module.network.BaseCloudAPIService
import com.module_social.api.VideoApiService
import com.module_social.api.repository.VideoRepository
import kotlinx.coroutines.launch
import org.w3c.dom.Comment

class VideoViewModel(baseActivity: BaseActivity?) : BaseViewModel() {

    private val videoRepository = VideoRepository(BaseCloudAPIService.getApiService(VideoApiService::class.java),baseActivity)

    internal val videoListLiveData = MutableLiveData<VideoListModel>()
    internal val videoCommentListModel = MutableLiveData<CommentListModel>()
    internal val videCommentModel = MutableLiveData<CommentModel>()

    fun getVideos(showBlockingLoader:Boolean){
        scope.launch {
            try {
                videoListLiveData.postValue(videoRepository.getVideos(showBlockingLoader))
            } catch (e: Throwable){
                //errorResponseLiveData.postValue(parseErrorResponseFromJson(e.message))
            }
        }
    }

    fun getVideoComments(post_id:String){
        scope.launch {
            try {
                videoCommentListModel.postValue(videoRepository.getVideoComments(post_id))
            } catch (e: Throwable){
                e.printStackTrace()
            }
        }
    }

    fun saveVideoComment(body:String){
        scope.launch {
            try {
                videCommentModel.postValue(videoRepository.saveVideoComment(body))
            } catch (e: Throwable){
                //errorResponseLiveData.postValue(parseErrorResponseFromJson(e.message))
            }
        }
    }


    fun markVideoView(postId:String){
        scope.launch {
            try {
                videoRepository.markVideoView(postId).let {
                    if(it!=null){
                        localCache?.updateVideoViewsCount(it)
                    }
                }
            } catch (e: Throwable){
                
            }
        }
    }

}