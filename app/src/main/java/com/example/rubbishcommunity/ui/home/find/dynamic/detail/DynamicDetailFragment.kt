package com.example.rubbishcommunity.ui.home.find.dynamic.detail

import android.app.Activity
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicDetailBinding
import com.example.rubbishcommunity.ui.*
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.base.hideInput
import com.example.rubbishcommunity.ui.base.showInput
import com.example.rubbishcommunity.ui.container.jumpToInnerComment
import com.example.rubbishcommunity.ui.home.find.dynamic.DynamicListGridImageAdapter
import com.example.rubbishcommunity.ui.home.find.dynamic.MyGridLayoutManager


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
		showInput(
			activity as Activity,
			binding.linComment.editComment
		)
	}
	
	//隐藏键盘及输入框
	private fun hideInputDialog() {
		if (mManager.isSoftKeyboardOpened) {
			//隐藏键盘
			hideInput(activity as Activity)
		}
	}
	
	
	override fun initBefore() {
		activity!!.intent.getStringExtra("dynamicId")
		viewModel.init()
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		//照片列表
		binding.imgRec.run {
			layoutManager =
				MyGridLayoutManager(context, 3)
			adapter = DynamicListGridImageAdapter(
				viewModel.imgList.value!!
			) { position ->
				showGallery(context, viewModel.imgList.value!!, position)
			}
		}
		
		//整体滑动监听
		binding.nestedscroll.setOnScrollChangeListener { _: NestedScrollView?, _: Int, _: Int, _: Int, _: Int ->
			//滚动时隐藏输入
			hideInputDialog()
		}
		
		
		//评论列表
		binding.commentRec.run {
			layoutManager = LinearLayoutManager(context)
			adapter = CommentListAdapter(
				viewModel.commentList.value!!, { position ->
					//查看内部评论
					jumpToInnerComment(
						context,
						viewModel.commentList.value!![position].innerCommentList ?: listOf()
					)
				}, { position ->
					showInputDialog()
					replyPosition = position
				}
			)
		}
		
		//键盘上方的发送按钮
		binding.linComment.btnSend.setOnClickListener {
			if (!viewModel.inputComment.value.isNullOrEmpty()) {
				viewModel.inputComment.postValue("")
				MyApplication.showToast("回复原文：${viewModel.inputComment.value}")
				hideInput(activity as Activity)
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
				hideInput(activity as Activity)
			} else {
				MyApplication.showToast("回复不能为空")
			}
		}
	}
	
	override fun initData() {
	
	}
	
	
}