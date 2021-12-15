package com.base_module.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CommentListModel(
    @SerializedName("comment_list")
    var comment_list:ArrayList<CommentModel>? = null,
)

data class CommentModel(
    @Expose
    @SerializedName("comment_id")
    var comment_id:String? = null ,

    @Expose
    @SerializedName("post_id")
    var post_id:String? = null ,

    @Expose
    @SerializedName("comment_text")
    var comment_text:String? = null,

    @Expose
    @SerializedName("author_profile_url")
    var author_profile_url:String? = null,

    @Expose
    @SerializedName("author_name")
    var author_name:String? = null,

    @Expose
    @SerializedName("communication_key")
    var communication_key:String? = null
)