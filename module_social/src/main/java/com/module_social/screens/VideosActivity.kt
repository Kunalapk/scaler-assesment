package com.module_social.screens

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.base_module.AppController.Companion.localCache
import com.base_module.base.BaseActivity
import com.base_module.extensions.removeFragmentFromBottom
import com.base_module.extensions.replaceFragmentFromBottom
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener
import com.kunalapk.smartrecyclerview.view.SmartRecyclerView
import com.module_social.R
import com.module_social.databinding.ActivityVideosBinding
import com.module_social.listeners.OnPostItemClickListener
import com.base_module.model.VideoModel
import com.module_social.comparators.VideoTitleComparator
import com.module_social.comparators.VideoViewsComparator
import com.module_social.screens.fragments.VideoPlayerFragment
import com.module_social.viewmodel.VideoViewModel
import java.util.*

class VideosActivity : BaseActivity() {

    private val _viewBinder by lazy { ActivityVideosBinding.inflate(layoutInflater) }

    private lateinit var rvVideos:SmartRecyclerView<VideoModel>
    private lateinit var videoVideoModel: VideoViewModel
    private var videoPlayerFragment: VideoPlayerFragment? = null
    private var toggleType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_viewBinder.root)

        setSupportActionBar(_viewBinder.toolBar)

    }

    override fun initView() {
        rvVideos = _viewBinder.rvVideos as SmartRecyclerView<VideoModel>
        rvVideos.apply {
            initSmartRecyclerView(this@VideosActivity,smartRecyclerViewListener,false)
            isEnabled = false
            setClickListener(onPostItemClickListener)
        }
    }

    override fun initViewModels() {
        videoVideoModel = getViewModel(activity = this, viewModel = VideoViewModel(this), className = VideoViewModel::class.java)
        videoVideoModel.getVideos(true)
    }

    override fun onViewClick(view: View?) {

    }

    override fun setListeners() {

    }

    override fun setObservers() {

        localCache?.subscribeToVideoList()?.observe(this, Observer {
            if(!it.isNullOrEmpty()){
                rvVideos.apply {
                    addItemsWithDiffUtil(it)
                    recyclerView?.post {
                        recyclerView?.scrollToPosition(0)
                    }
                }
            }
        })

        videoVideoModel.videoListLiveData.observe(this, Observer {
            if(it.video_list!=null){
                Collections.shuffle(it.video_list)
                localCache?.deleteAllVideos()
                localCache?.saveVideoList(it.video_list!!)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_videos,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_sort -> {
                toggleSort()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleSort(){
        when(toggleType){
            0 -> {
                rvVideos.apply{
                    getItems().sortWith(VideoTitleComparator())
                    notifyDataSetChanged()
                }
                toggleType = 1
                showToast("Sorted by name")
            }
            1 -> {
                rvVideos.apply{
                    getItems().sortWith(VideoViewsComparator())
                    notifyDataSetChanged()
                }
                toggleType = 0
                showToast("Sorted by views")
            }
        }
    }

    private val onPostItemClickListener = object:OnPostItemClickListener{
        override fun onPostItemClick(videoModel: VideoModel?) {
            if(videoModel!=null){
                showVideoPlayerFragment(videoModel)
            }
        }
    }

    fun showVideoPlayerFragment(videoModel: VideoModel){
        videoPlayerFragment = VideoPlayerFragment.getInstance()

        supportFragmentManager.removeFragmentFromBottom(fragment = videoPlayerFragment!!)
        videoPlayerFragment?.arguments = VideoPlayerFragment.getBundle(videoModel)
        supportFragmentManager.replaceFragmentFromBottom(R.id.flVideoContainer,videoPlayerFragment!!,VideoPlayerFragment.TAG)

    }

    private val smartRecyclerViewListener = object : SmartRecyclerViewListener<VideoModel>{

        override fun getViewLayout(key: Int): Int {
            return R.layout.item_video_post
        }

        override fun getItemViewType(model: VideoModel): Int {
            return 0
        }

        override fun areContentsTheSame(newItem: VideoModel, oldItem: VideoModel): Boolean {
            return newItem.post_text==oldItem.post_text
                    && newItem.post_file_url==oldItem.post_file_url
                    && newItem.post_thumbnail_url==oldItem.post_thumbnail_url
                    && newItem.post_views_count==oldItem.post_views_count
        }

        override fun areItemsTheSame(newItem: VideoModel, oldItem: VideoModel): Boolean {
            return newItem.post_id==oldItem.post_id
        }
    }

    override fun onBackPressed() {
        if(videoPlayerFragment?.fullscreen==true)
            videoPlayerFragment?.toggleFullScreen()
        else if(videoPlayerFragment?.videoCommentsFragment?.isAdded==true)
            videoPlayerFragment?.removeCommentFragment()
        else if(videoPlayerFragment?.isAdded==true)
            supportFragmentManager.removeFragmentFromBottom(videoPlayerFragment!!)
        else
            super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayerFragment?.destroyVideoObjects()
        videoPlayerFragment = null
    }

}