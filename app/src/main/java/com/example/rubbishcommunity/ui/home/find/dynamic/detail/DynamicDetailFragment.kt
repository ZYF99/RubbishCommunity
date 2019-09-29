package com.example.rubbishcommunity.ui.home.find.dynamic.detail

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicDetailBinding
import com.example.rubbishcommunity.model.Comment
import com.example.rubbishcommunity.ui.*
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.ui.container.jumpToInnerComment
import com.example.rubbishcommunity.ui.home.find.dynamic.DynamicListGridImageAdapter
import com.example.rubbishcommunity.ui.release.dynamic.MyGridLayoutManager
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

class DynamicDetailFragment : BindingFragment<DynamicDetailBinding, DynamicDetailViewModel>(
	DynamicDetailViewModel::class.java, R.layout.fragment_dynamic_detail
) {
	
	
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
						//回复
						
					}
					
				}
			)
		}
		
		//返回按钮
		RxView.clicks(binding.btnBack).doOnNext {
			activity?.finish()
		}.throttleFirst(2, TimeUnit.SECONDS)
			.bindLife()
		
		//评论按钮
		RxView.clicks(binding.btnComment)
			.throttleFirst(2, TimeUnit.SECONDS).doOnNext {
				binding.frameComment.visibility = View.VISIBLE
				showInput(
					activity as Activity,
					binding.frameComment,
					binding.editComment
				)

				
			}.bindLife()
		
	}
	
	override fun initData() {
	
	}
	
	
}