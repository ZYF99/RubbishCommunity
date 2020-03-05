package com.example.rubbishcommunity.ui.home.find.moment.detail


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CommentCellBinding
import com.example.rubbishcommunity.model.Comment
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter


class CommentListAdapter(
	val list: MutableList<Comment>,
	private val onLookCommentClick: (Int) -> Unit,
	private val onReplyClick: (Int) -> Unit
) :
	BaseRecyclerAdapter<Comment, CommentCellBinding>(
		R.layout.cell_comment,
		{
		
		}
	) {
	override val baseList: MutableList<Comment>
		get() = list
	
	override fun bindData(binding: CommentCellBinding, position: Int) {
		binding.comment = list[position]
		//查看内部评论按钮
		binding.btnLookcomment.setOnClickListener {
			onLookCommentClick(position)
		}
		//回复按钮
		binding.btnReply.setOnClickListener {
			onReplyClick(position)
		}
	}
	
	
	
}