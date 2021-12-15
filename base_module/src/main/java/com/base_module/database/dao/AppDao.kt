package com.base_module.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.base_module.model.VideoModel

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideos(videos: ArrayList<VideoModel>)

    @Query("SELECT * from tb_videos")
    fun getVideosList():LiveData<MutableList<VideoModel>>

    @Query("SELECT * from tb_videos where post_id!=:post_id LIMIT 5")
    fun getVideosList(post_id:String):LiveData<MutableList<VideoModel>>

    @Query("UPDATE tb_videos set post_views_count=:count where post_id=:post_id")
    fun updateVideoViewsCount(count:Long,post_id:String)


    @Query("DELETE from tb_videos")
    fun deleteAllVideos()
}