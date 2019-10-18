package com.example.rubbishcommunity.ui.home.find.dynamic.detail

import android.app.Activity
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicDetailBinding
import com.example.rubbishcommunity.ui.*
import com.example.rubbishcommunity.ui.container.jumpToInnerComment
import com.example.rubbishcommunity.ui.home.find.dynamic.DynamicListGridImageAdapter
import com.example.rubbishcommunity.ui.release.dynamic.MyGridLayoutManager
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

class DynamicDetailFragment : BindingFragment<DynamicDetailBinding, DynamicDetailViewModel>(
	DynamicDetailViewModel::class.java, R.layout.fragment_dynamic_detail
) {
	//键盘收起的回调
	override fun onSoftKeyboardClosed() {
		super.onSoftKeyboardClosed()
		binding.cardBtn.visibility = View.VISIBLE
		binding.linComment.root.visibility = View.GONE
	}
	
	//发送信息的位置
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
			layoutManager = MyGridLayoutManager(context, 3)
			adapter = DynamicListGridImageAdapter(
				viewModel.imgList.value!!,
				object : DynamicListGridImageAdapter.OnItemClickListener {
					override fun onItemClick(position: Int, v: View) {
						showGallery(context, viewModel.imgList.value!!, position)
					}
				}
			)
		}
		
		//整体滑动监听
		binding.nestedscroll.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
			//滚动时隐藏输入
			hideInputDialog()
		}
		
		//评论列表
		binding.commentRec.run {
			layoutManager = LinearLayoutManager(context)
			adapter = CommentListAdapter(
				viewModel.commentList.value!!,
				object : CommentListAdapter.OnClickListener {
					override fun onLookCommentClick(position: Int) {
						//查看内部评论
						(viewModel.commentList.value?.get(position)?.innerCommentList)?.let { innerCommentList ->
							jumpToInnerComment(context, innerCommentList)
						}
					}
					
					override fun onoReplyClick(position: Int) {
						showInputDialog()
						replyPosition = position
					}
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
		
		//评论按钮
		RxView.clicks(binding.btnComment).doOnNext {
			replyPosition = null
			showInputDialog()
		}.throttleFirst(2, TimeUnit.SECONDS)
			.bindLife()
		
		//发送按钮 回复原文
		RxView.clicks(binding.linComment.btnSend).doOnNext {
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
		}.throttleFirst(2, TimeUnit.SECONDS)
			.bindLife()
		

		
	}
	
	override fun initData() {
	
	}
	
	
}