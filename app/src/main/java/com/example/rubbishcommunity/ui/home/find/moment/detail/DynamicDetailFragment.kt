package com.example.rubbishcommunity.ui.home.find.moment.detail

import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicDetailBinding
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.container.jumpToInnerComment
import com.example.rubbishcommunity.ui.home.find.moment.MomentsListGridImageAdapter
import com.example.rubbishcommunity.ui.home.find.moment.getMomentsPictureLayoutManager
import com.example.rubbishcommunity.ui.utils.hideSoftKeyBoard
import com.example.rubbishcommunity.ui.utils.openSoftKeyBoard
import com.example.rubbishcommunity.ui.utils.showGallery
import com.example.rubbishcommunity.utils.globalGson
import timber.log.Timber


class DynamicDetailFragment : BindingFragment<DynamicDetailBinding, DynamicDetailViewModel>(
	DynamicDetailViewModel::class.java, R.layout.fragment_dynamic_detail
) {
	//键盘收起的回调
	override fun onSoftKeyboardClosed() {
		super.onSoftKeyboardClosed()
		binding.cardBtn.visibility = View.VISIBLE
		binding.linComment.root.visibility = View.GONE
	}
	
	//发送信息的index
	var replyPosition: Int? = null
	
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
		
	}
	
	override fun initWidget() {
		binding.vm = viewModel
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
			Timber.d("~~~~~~~~${viewModel.moment.value?.momentCommentList}")
			layoutManager = LinearLayoutManager(context)
			adapter = CommentListAdapter(
				{ position ->
					//查看内部评论
					jumpToInnerComment(
						context,
						commentList[position].commentReplyList
					)
				}, { position ->
					showInputDialog()
					replyPosition = position
				},
				commentList
			)
		}
		
		//键盘上方的发送按钮
		binding.linComment.btnSend.setOnClickListener {
			if (!viewModel.inputComment.value.isNullOrEmpty()) {
				viewModel.inputComment.postValue("")
				MyApplication.showToast("回复原文：${viewModel.inputComment.value}")
				activity!!.hideSoftKeyBoard()
			} else {
				MyApplication.showToast("回复不能为空")
			}
		}
		
		//返回按钮
		binding.btnBack.setOnClickListener {
			activity?.finish()
		}
		
		//评论按钮
		binding.btnComment.setOnClickListener {
			replyPosition = null
			showInputDialog()
		}
		
		//发送按钮 回复原文
		binding.linComment.btnSend.setOnClickListener {
			if (!viewModel.inputComment.value.isNullOrEmpty()) {
				viewModel.inputComment.postValue("")
				when (replyPosition) {
					null -> MyApplication.showToast("回复原文 ：${viewModel.inputComment.value}")
					else -> MyApplication.showToast("回复第${replyPosition}条评论 ：${viewModel.inputComment.value}")
				}
				activity!!.hideSoftKeyBoard()
			} else {
				MyApplication.showToast("回复不能为空")
			}
		}
	}
	
	override fun initData() {
	
	}
	
}