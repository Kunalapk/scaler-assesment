package com.module_social.comparators

import com.base_module.model.VideoModel

class VideoViewsComparator: Comparator<VideoModel>{

    override fun compare(p0: VideoModel?, p1: VideoModel?): Int {
        if(p0 == null || p1 == null)
            return 0
        return p0.post_views_count.compareTo(p1.post_views_count)
    }
}
