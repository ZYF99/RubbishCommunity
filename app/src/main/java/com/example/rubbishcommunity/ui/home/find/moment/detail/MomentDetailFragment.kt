package com.example.rubbishcommunity.ui.home.find.moment.detail

import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FragmentMomentDetailBinding
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.home.find.moment.MomentsListGridImageAdapter
import com.example.rubbishcommunity.ui.home.find.moment.getMomentsPictureLayoutManager
import com.example.rubbishcommunity.ui.utils.hideSoftKeyBoard
import com.example.rubbishcommunity.ui.utils.openSoftKeyBoard
import com.example.rubbishcommunity.ui.utils.showGallery
import com.example.rubbishcommunity.utils.globalGson
import timber.log.Timber


class MomentDetailFragment : BindingFragment<FragmentMomentDetailBinding, MomentDetailViewModel>(
	MomentDetailViewModel::class.java, R.layout.fragment_moment_detail
) {
	//键盘收起的回调
	override fun onSoftKeyboardClosed() {
		super.onSoftKeyboardClosed()
		binding.cardBtn.visibility = View.VISIBLE
		binding.linComment.root.visibility = View.GONE
	}
	
	//发送信息的index
	private var replyCommentId: Long? = null
	
	//弹出键盘及输入框
	private fun showInputDialog() {
		//显示输入框，隐藏下方按钮
		binding.cardBtn.visibility = View.GONE
		binding.linComment.root.visibility = View.VISIBLE
		//弹出键盘
		activity!!.openSoftKeyBoard(binding.linComment.editComment)
		
	}
	
	//隐藏键盘及输入框
	private fun hideInputDialog() {
		if (mManager.isSoftKeyboardOpened) {
			//隐藏键盘
			activity!!.hideSoftKeyBoard()
		}
	}
	
	
	override fun initBefore() {
		viewModel.moment.value =
			globalGson.fromJson<MomentContent>(
				activity!!.intent.getStringExtra(
					"moment"
				), MomentContent::class.java
			)
		binding.vm = viewModel
	}
	
	override fun initWidget() {
		viewModel.moment.observeNonNull {
			(binding.imgRec.adapter as MomentsListGridImageAdapter).replaceData(it.pictures)
			(binding.commentRec.adapter as CommentListAdapter).replaceData(it.realCommentList?: emptyList())
		}
		//照片列表
		binding.imgRec.run {
			val pictures = viewModel.moment.value?.pictures
			layoutManager = getMomentsPictureLayoutManager(context, pictures?.size ?: 1)
			adapter =
				MomentsListGridImageAdapter(pictures ?: emptyList()) { position ->
					showGallery(
						context,
						pictures ?: emptyList(),
						position
					)
				}
		}
		
		//整体滑动监听
		binding.nestedscroll.setOnScrollChangeListener { _: NestedScrollView?, _: Int, _: Int, _: Int, _: Int ->
			//滚动时隐藏输入
			hideInputDialog()
		}
		
		//评论列表
		binding.commentRec.run {
			val commentList = viewModel.moment.value?.realCommentList ?: emptyList()
			layoutManager = LinearLayoutManager(context)
			adapter = CommentListAdapter(
				{ comment ->
					showInputDialog()
					replyCommentId = comment.commentId
				},
				commentList
			)
		}
		
		//返回按钮
		binding.btnBack.setOnClickListener {
			activity?.finish()
		}
		
		//评论按钮
		binding.btnComment.setOnClickListener {
			showInputDialog()
		}
		
		//点赞按钮
		binding.btnLike.setOnClickListener{
			viewModel.like()
		}
		
		//发送按钮 回复原文
		binding.linComment.btnSend.setOnClickListener {
			if (!viewModel.inputComment.value.isNullOrEmpty()) {
				viewModel.inputComment.postValue("")
				when (replyCommentId) {
					null -> viewModel.pushComment(viewModel.moment.value?.momentId ?: 0.toLong())
					else -> {
						viewModel.replyComment(replyCommentId)
						replyCommentId = null
					}
				}
				activity!!.hideSoftKeyBoard()
			} else {
				MyApplication.showWarning("回复不能为空")
			}
		}
		
		//转发按钮
		binding.btnTrans.setOnClickListener{
			viewModel.forward(viewModel.moment.value?.momentId?:0)
		}
		
	}
	
	override fun initData() {
	
	}
	
}