package com.module_social.api

import com.base_module.constants.BaseAPIConstants
import com.base_module.model.CommentListModel
import com.base_module.model.CommentModel
import com.base_module.model.VideoListModel
import com.base_module.model.VideoModel
import com.base_module.network.api.BaseApiService
import retrofit2.Response
import retrofit2.http.*


interface VideoApiService: BaseApiService {

    @Headers(BaseAPIConstants.CONTENT_TYPE_JSON)
    @GET(BaseAPIConstants.API_VIDEOS)
    suspend fun getVideos(): Response<VideoListModel>


    @Headers(BaseAPIConstants.CONTENT_TYPE_JSON)
    @GET(BaseAPIConstants.API_VIDEO_VIEWS_COUNT)
    suspend fun markVideoView(@Query("post_id") post_id:String): Response<VideoModel>


    @Headers(BaseAPIConstants.CONTENT_TYPE_JSON)
    @GET(BaseAPIConstants.API_VIDEO_COMMENT)
    suspend fun getVideoComments(@Query("post_id") post_id:String): Response<CommentListModel>


    @Headers(BaseAPIConstants.CONTENT_TYPE_JSON)
    @POST(BaseAPIConstants.API_VIDEO_COMMENT)
    suspend fun saveVideoComment(@Body body:String): Response<CommentModel>

}