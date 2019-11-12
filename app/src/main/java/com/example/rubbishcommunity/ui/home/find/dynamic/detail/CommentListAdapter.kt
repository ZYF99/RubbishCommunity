package com.example.rubbishcommunity.ui.home.find.dynamic.detail


import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CommentCellBinding
import com.example.rubbishcommunity.model.Comment



class CommentListAdapter(
	data: List<Comment>?,
	private val onLookCommentClick: (Int) -> Unit,
	private val onReplyClick: (Int) -> Unit
) :
	BaseQuickAdapter<Comment, BaseViewHolder>(R.layout.cell_comment, data) {
	
	override fun convert(helper: BaseViewHolder, comment: Comment) {
		val binding: CommentCellBinding = DataBindingUtil.bind(helper.itemView)!!
		binding.comment = comment
		
		//查看内部评论按钮
		binding.btnLookcomment.setOnClickListener {
			onLookCommentClick.invoke(helper.position)
		}
		//回复按钮
		binding.btnReply.setOnClickListener {
			onReplyClick.invoke(helper.position)
		}
	}
	
	
}