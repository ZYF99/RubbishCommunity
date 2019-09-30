package com.example.rubbishcommunity.ui.home.find.dynamic.detail


import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CommentCellBinding
import com.example.rubbishcommunity.model.Comment
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit


class CommentListAdapter(data: List<Comment>?, private val onClickListener: OnClickListener) :
	BaseQuickAdapter<Comment, BaseViewHolder>(R.layout.cell_comment, data) {
	
	override fun convert(helper: BaseViewHolder, comment: Comment) {
		val binding: CommentCellBinding = DataBindingUtil.bind(helper.itemView)!!
		binding.comment = comment
		//查看内部评论按钮
		RxView.clicks(binding.btnLookcomment).doOnNext {
			onClickListener.onLookCommentClick(helper.position)
		}.throttleFirst(2, TimeUnit.SECONDS).subscribe()
		
		//回复按钮
		RxView.clicks(binding.btnReply).doOnNext {
			onClickListener.onoReplyClick(helper.position)
		}.throttleFirst(2,TimeUnit.SECONDS).subscribe()
	}
	
	interface OnClickListener {
		fun onLookCommentClick(position: Int)
		fun onoReplyClick(position: Int)
		
	}
	
}