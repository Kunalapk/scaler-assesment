package com.base_module.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class VideoListModel (
    @SerializedName("video_list")
    var video_list:ArrayList<VideoModel>? = null,
)

@Entity(tableName = "tb_videos")
data class VideoModel (
    @PrimaryKey
    @SerializedName("post_id")
    var post_id:String,

    @SerializedName("post_thumbnail_url")
    var post_thumbnail_url:String? = null,

    @SerializedName("post_file_url")
    var post_file_url:String? = null,

    @SerializedName("post_author_url")
    var post_author_url:String? = null,

    @SerializedName("post_text")
    var post_text:String? = null,

    @SerializedName("post_video_duration")
    var post_video_duration:String? = null,

    @SerializedName("post_views_count")
    var post_views_count:Long = 0,

    @SerializedName("post_thumbnail_width")
    var post_thumbnail_width:Int = 0,

    @SerializedName("post_thumbnail_height")
    var post_thumbnail_height:Int = 0,
):Serializable