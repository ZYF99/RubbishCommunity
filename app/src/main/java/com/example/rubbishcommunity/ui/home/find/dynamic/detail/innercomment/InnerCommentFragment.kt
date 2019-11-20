package com.example.rubbishcommunity.ui.home.find.dynamic.detail.innercomment

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.InnerCommentBinding
import com.example.rubbishcommunity.model.Comment
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.container.jumpToInnerComment
import com.example.rubbishcommunity.ui.base.hideInput
import com.example.rubbishcommunity.ui.home.find.dynamic.detail.CommentListAdapter
import com.example.rubbishcommunity.ui.base.showInput
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
class InnerCommentFragment : BindingFragment<InnerCommentBinding, InnerCommentViewModel>(
	InnerCommentViewModel::class.java, R.layout.fragment_inner_comment_list
) {
	
	//发送信息的位置
	var replyPosition: Int = 0
	
	//键盘收起的回调
	override fun onSoftKeyboardClosed() {
		super.onSoftKeyboardClosed()
		binding.linComment.root.visibility = View.GONE
	}
	
	//弹出键盘及输入框
	private fun showInputDialog() {
		//显示输入框，隐藏下方按钮
		binding.linComment.root.visibility = View.VISIBLE
		//弹出键盘
		showInput(
			activity as Activity,
			binding.linComment.editComment
		)
	}
	
	override fun initBefore() {
		viewModel.run { init((activity!!.intent.getSerializableExtra("commentList")) as List<Comment>) }
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		//评论列表
		binding.recInnerComment.run {
			layoutManager = LinearLayoutManager(context)
			adapter = CommentListAdapter(
				viewModel.innerCommentList.value!!,
				{ position ->
					(viewModel.innerCommentList.value?.get(position)?.innerCommentList)?.let { innerCommentList ->
						jumpToInnerComment(context, innerCommentList)
					}
				}, { position ->
					//回复
					showInputDialog()
					replyPosition = position
				}
			)
		}
		
		//键盘上方的发送按钮
		RxView.clicks(binding.linComment.btnSend).doOnNext {
			if (!viewModel.inputComment.value.isNullOrEmpty()) {
				viewModel.inputComment.postValue("")
				MyApplication.showToast("回复原文：${viewModel.inputComment.value}")
				hideInput(activity as Activity)
			} else {
				MyApplication.showToast("回复不能为空")
			}
		}.throttleFirst(2, TimeUnit.SECONDS)
			.bindLife()
		
		
		//返回按钮
		RxView.clicks(binding.btnBack).doOnNext {
			activity?.finish()
		}.throttleFirst(2, TimeUnit.SECONDS)
			.bindLife()
		
		
	}
	
	override fun initData() {
	
	}
	
	
}