package com.module_social.screens.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base_module.account_module.viewmodel.AccountViewModel
import com.base_module.base.BaseActivity
import com.base_module.base.BaseFragment
import com.base_module.constants.BaseAppConstants
import com.base_module.databinding.FragmentMobileOtpVerificationBinding
import com.base_module.helpers.LogHelper
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.module_social.databinding.FragmentVideoPlayerBinding
import com.base_module.model.VideoModel
import com.module_social.viewmodel.VideoViewModel
import android.content.pm.ActivityInfo
import android.os.Build
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.module_social.R
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.base_module.AppController.Companion.localCache
import com.base_module.extensions.*
import com.google.android.exoplayer2.Player
import com.kunalapk.smartrecyclerview.adapter.CustomAdapter
import com.module_social.listeners.OnPostItemClickListener
import com.module_social.screens.VideosActivity
import com.google.android.exoplayer2.PlaybackParameters

import com.google.android.exoplayer2.ExoPlaybackException

import com.google.android.exoplayer2.trackselection.TrackSelectionArray

import com.google.android.exoplayer2.source.TrackGroupArray

import com.google.android.exoplayer2.Timeline
import android.R.id
import android.media.Image
import com.base_module.AppController.Companion.simpleCache
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File


class VideoPlayerFragment : BaseFragment() {

    companion object{
        var TAG = BaseFragment::class.java.simpleName

        fun getInstance():VideoPlayerFragment{
            return VideoPlayerFragment()
        }

        fun getBundle(videoModel: VideoModel):Bundle{
            return Bundle().apply {
                putSerializable(BaseAppConstants.KEY_VIDEO_MODEL,videoModel)
            }
        }
    }

    private var customAapter:CustomAdapter<VideoModel>? = null

    private lateinit var videoViewModel: VideoViewModel
    private var _viewBinder: FragmentVideoPlayerBinding? = null
    private val exoPlayer: ExoPlayer by lazy { ExoPlayer.Builder(requireContext()).build()}
    private var liveDataVideoModel: LiveData<MutableList<VideoModel>>? = null

    var fullscreen = false
    var videoCommentsFragment:VideoCommentsFragment? = null

    private var videoModel:VideoModel? = null
        get() {
            return arguments?.getSerializable(BaseAppConstants.KEY_VIDEO_MODEL) as VideoModel?
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinder = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        return _viewBinder?.root
    }

    override fun initViewModels() {
        videoViewModel = getViewModel(fragment = this,viewModel = VideoViewModel(activity as BaseActivity),className = VideoViewModel::class.java)
    }

    override fun initView(view: View) {
        if(videoModel==null) {
            return
        }
        videoViewModel.markVideoView(videoModel!!.post_id)

        customAapter = CustomAdapter<VideoModel>(activity =  activity as AppCompatActivity,isPaginated = false).apply {
            setItemLayout(R.layout.item_video_post_suggestions)
            setOnClickListener(onPostItemClickListener)
        }
        _viewBinder?.rvVideoSuggestions?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = customAapter
        }
        _viewBinder?.ivAuthorUrl?.loadImage(videoModel?.post_author_url)
        _viewBinder?.tvTitle?.text = videoModel?.post_text
        _viewBinder?.tvViews?.text = (videoModel!!.post_views_count+1).toString() +" Views"
        setUpExoPlayer()
    }

    private fun setUpExoPlayer(){
        _viewBinder?.playerView?.apply {
            player = exoPlayer
        }

        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true))
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        exoPlayer.setMediaSource(
            ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(videoModel?.post_file_url))))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun setListeners() {
        setOnClickListener(_viewBinder?.playerView?.findViewById<ImageView>(R.id.exo_fullscreen_icon))
        setOnClickListener(_viewBinder?.playerView?.findViewById<ImageView>(R.id.ivSpeaker))
        setOnClickListener(_viewBinder?.ivComment)
        exoPlayer.addListener(eventListener)
    }

    override fun onViewClick(view: View?) {
        when(view?.id){
            R.id.exo_fullscreen_icon -> {
                toggleFullScreen()
            }
            R.id.ivComment -> {
                showVideoComments()
            }
            R.id.ivSpeaker -> {
                toggleVolume()
            }
        }
    }

    override fun setObservers() {
        if(videoModel?.post_id!=null){
            liveDataVideoModel = localCache?.subscribeToOtherVideoList(videoModel?.post_id!!)
            liveDataVideoModel?.observeForever(videoModelObserver)
        }
    }

    private fun toggleVolume(){
        val currentVolume: Float = exoPlayer.volume
        if (currentVolume == 0f){
            exoPlayer.volume = 1f
            _viewBinder?.playerView?.findViewById<ImageView>(R.id.ivSpeaker)?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_volume_up_24))
        } else {
            exoPlayer.volume = 0f
            _viewBinder?.playerView?.findViewById<ImageView>(R.id.ivSpeaker)?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_volume_off_24))
        }
    }
    private val videoModelObserver = Observer<MutableList<VideoModel>> { it ->
        if(customAapter?.getItems()?.size==0){
            customAapter?.addItems(it as MutableList<Any>)
        }
    }

    private val onPostItemClickListener = object : OnPostItemClickListener{
        override fun onPostItemClick(videoModel: VideoModel?) {
            if(activity is VideosActivity && videoModel!=null){
                (activity as VideosActivity).showVideoPlayerFragment(videoModel)
            }
        }
    }

    private fun showVideoComments(){
        if(videoModel!=null){
            if(videoCommentsFragment==null){
                videoCommentsFragment = VideoCommentsFragment.getInstance(videoModel!!)
            }

            if(videoCommentsFragment?.isAdded==false){
                childFragmentManager.replaceFragmentFromBottom(R.id.flCommentsContainer,videoCommentsFragment!!,VideoCommentsFragment.TAG)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LogHelper.debug(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        LogHelper.debug(TAG,"onPause")
        playWhenReady(false)

    }

    override fun onDestroy() {
        super.onDestroy()
        LogHelper.debug(TAG,"onDestroy")
        liveDataVideoModel?.removeObserver(videoModelObserver)
        customAapter?.clearItems()
        videoCommentsFragment = null
        destroyVideoObjects()
        playWhenReady(false)
    }

    fun removeCommentFragment(){
        if(videoCommentsFragment!=null){
            childFragmentManager.removeFragmentFromBottom(videoCommentsFragment!!)
        }
    }

    private fun playWhenReady(status:Boolean){
        exoPlayer.playWhenReady = status
    }


    fun toggleFullScreen(){
        if(context==null)
            return

        if (fullscreen) {
            _viewBinder?.playerView?.findViewById<ImageView>(R.id.exo_fullscreen_icon)?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_fullscreen_24))
            showSystemUI()
            activity?.hideShowActionBar(true)
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val params = _viewBinder?.playerView?.layoutParams as ConstraintLayout.LayoutParams
            params.height = 0
            params.dimensionRatio = "H,16,9"
            _viewBinder?.playerView?.layoutParams = params

            fullscreen = false
        } else {
            _viewBinder?.playerView?.findViewById<ImageView>(R.id.exo_fullscreen_icon)?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_fullscreen_exit_24))
            hideSystemUI()
            activity?.hideShowActionBar(false)
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            _viewBinder?.playerView?.layoutParams = _viewBinder?.playerView?.layoutParams?.apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            fullscreen = true
        }
    }

    private fun hideSystemUI() {
        if(activity!=null){
            WindowInsetsControllerCompat(activity!!.window, activity!!.window.decorView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    private fun showSystemUI() {
        if(activity!=null){
            WindowCompat.setDecorFitsSystemWindows(activity!!.window, true)
            WindowInsetsControllerCompat(activity!!.window, activity!!.window.decorView).show(WindowInsetsCompat.Type.systemBars())
        }
    }

    fun destroyVideoObjects(){
        exoPlayer.release()
    }

    private val eventListener: Player.Listener = object : Player.Listener {

        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {

        }

        override fun onLoadingChanged(isLoading: Boolean) {

        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {

                Player.STATE_ENDED -> {

                }

                Player.STATE_READY -> {
                    _viewBinder?.progressBar?.visibility = View.GONE
                }

                Player.STATE_BUFFERING -> {
                    _viewBinder?.progressBar?.visibility = View.VISIBLE
                }

                Player.STATE_IDLE -> {

                }
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {

        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

        }

        override fun onPositionDiscontinuity(reason: Int) {

        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

        }

        override fun onSeekProcessed() {

        }
    }

}