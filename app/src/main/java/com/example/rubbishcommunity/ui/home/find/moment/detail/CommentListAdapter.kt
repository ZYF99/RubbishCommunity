package com.example.rubbishcommunity.ui.home.find.moment.detail

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CommentCellBinding
import com.example.rubbishcommunity.model.api.moments.MomentComment
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class CommentListAdapter(
	private val onLookCommentClick: (Int) -> Unit,
	private val onReplyClick: (Int) -> Unit,
	list: List<MomentComment>
) : BaseRecyclerAdapter<MomentComment, CommentCellBinding>(
		R.layout.cell_comment,
		{
		
		},
		baseList = list
	) {
	
	override fun bindData(binding: CommentCellBinding, position: Int) {
		binding.comment = baseList[position]
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