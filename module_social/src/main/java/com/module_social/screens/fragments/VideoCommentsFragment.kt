package com.module_social.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base_module.base.BaseActivity
import com.base_module.base.BaseFragment
import com.base_module.constants.BaseAppConstants
import com.base_module.helpers.LogHelper
import com.base_module.model.VideoModel
import com.module_social.viewmodel.VideoViewModel
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.module_social.R
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.base_module.constants.SharedPrefConstant
import com.base_module.extensions.hideKeyBoard
import com.base_module.helpers.SharedPreferencesHelper
import com.base_module.model.CommentModel
import com.base_module.utils.GSONUtility
import com.kunalapk.smartrecyclerview.adapter.CustomAdapter
import com.module_social.databinding.FragmentVideoCommentsBinding

class VideoCommentsFragment : BaseFragment() {

    companion object{
        var TAG = BaseFragment::class.java.simpleName

        fun getInstance(videoModel: VideoModel):VideoCommentsFragment{
            return VideoCommentsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BaseAppConstants.KEY_VIDEO_MODEL,videoModel)
                }
            }
        }

    }

    private var customAapter:CustomAdapter<CommentModel>? = null

    private lateinit var videoViewModel: VideoViewModel
    private var _viewBinder: FragmentVideoCommentsBinding? = null

    var fullscreen = false

    private var videoModel:VideoModel? = null
        get() {
            return arguments?.getSerializable(BaseAppConstants.KEY_VIDEO_MODEL) as VideoModel?
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinder = FragmentVideoCommentsBinding.inflate(inflater, container, false)
        return _viewBinder?.root
    }

    override fun initViewModels() {
        videoViewModel = getViewModel(fragment = this,viewModel = VideoViewModel(activity as BaseActivity),className = VideoViewModel::class.java)

        if(videoModel?.post_id!=null){
            videoViewModel.getVideoComments(videoModel?.post_id!!)
        }
    }

    override fun initView(view: View) {
        customAapter = CustomAdapter<CommentModel>(activity =  activity as AppCompatActivity,isPaginated = false).apply {
            setItemLayout(R.layout.item_video_comment)
        }
        _viewBinder?.rvVideoComments?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = customAapter
        }

    }

    override fun setListeners() {
        _viewBinder?.etCommentBox?.addTextChangedListener(onTextChangeListener)
        setOnClickListener(_viewBinder?.ivSendMessage)
    }

    override fun onViewClick(view: View?) {
        when(view?.id){
            R.id.ivSendMessage -> {
                sendCommentToServer()
            }
        }
    }

    private fun sendCommentToServer(){
        if(!_viewBinder?.etCommentBox?.text.toString().isNullOrEmpty()){
            CommentModel(
                comment_text = _viewBinder?.etCommentBox?.text.toString(),
                post_id = videoModel?.post_id,
                communication_key = System.currentTimeMillis().toString(),
                author_name = SharedPreferencesHelper.getString(SharedPrefConstant.USER_NAME),
                author_profile_url = SharedPreferencesHelper.getString(SharedPrefConstant.PROFILE_ICON)
            ).let {
                updateCommentList(it)
                videoViewModel.saveVideoComment(GSONUtility.getJSON(it))
            }
            context?.hideKeyBoard(_viewBinder?.etCommentBox)
            _viewBinder?.etCommentBox?.text?.clear()
        }
    }

    override fun setObservers() {
        videoViewModel.videoCommentListModel.observe(this, Observer {
            if(it.comment_list!=null){
                customAapter?.addItems(it.comment_list as MutableList<Any>)
            }
        })
        videoViewModel.videCommentModel.observe(this, Observer {
            if(it!=null){
                updateCommentList(it)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        LogHelper.debug(TAG,"onResume")
    }

    override fun onPause() {
        super.onPause()
        LogHelper.debug(TAG,"onPause")

    }

    private val onTextChangeListener = object: TextWatcher{
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            _viewBinder?.ivSendMessage?.isEnabled = !s.isNullOrEmpty()
        }
    }

    private fun updateCommentList(commentModel: CommentModel){
        var found = false
        customAapter?.getItems().let{ list ->
            if(list!=null){
                (0 until list.size)
                    .map { list[it] }
                    .filter { it.communication_key==commentModel.communication_key}
                    .forEach {
                        list.indexOf(it).let { index ->
                            if(index!=-1){
                                found = true
                                customAapter?.setItem(index,it)
                            }
                        }
                    }
            }
        }
        if(!found){
            customAapter?.apply{
                addItem(0,commentModel)
                _viewBinder?.rvVideoComments?.scrollToPosition(0)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        LogHelper.debug(TAG,"onDestroy")
        customAapter?.clearItems(false)
    }


}