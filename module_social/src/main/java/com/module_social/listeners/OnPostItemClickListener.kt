package com.module_social.listeners

import com.base_module.model.VideoModel


interface OnPostItemClickListener {
    fun onPostItemClick(videoModel: VideoModel?)
}