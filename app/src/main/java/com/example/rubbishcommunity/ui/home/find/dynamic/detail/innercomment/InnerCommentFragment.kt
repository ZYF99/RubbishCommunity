package com.example.rubbishcommunity.ui.home.find.dynamic.detail.innercomment

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.InnerCommentBinding
import com.example.rubbishcommunity.model.Comment
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.ui.container.jumpToInnerComment
import com.example.rubbishcommunity.ui.home.find.dynamic.detail.CommentListAdapter
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
class InnerCommentFragment : BindingFragment<InnerCommentBinding, InnerCommentViewModel>(
	InnerCommentViewModel::class.java, R.layout.fragment_inner_comment_list
) {
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
				object :
					CommentListAdapter.OnClickListener {
					override fun onLookCommentClick(position: Int) {
						//查看内部评论
						(viewModel.innerCommentList.value?.get(position)?.innerCommentList)?.let { innerCommentList ->
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
		
		
	}
	
	override fun initData() {
	
	}
	
	
}