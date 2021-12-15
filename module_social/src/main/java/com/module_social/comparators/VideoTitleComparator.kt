package com.module_social.comparators

import com.base_module.model.VideoModel

class VideoTitleComparator: Comparator<VideoModel>{

    override fun compare(p0: VideoModel?, p1: VideoModel?): Int {
        if(p0?.post_text == null || p1?.post_text == null)
            return 0
        return p0.post_text!!.compareTo(p1.post_text!!)
    }
}
