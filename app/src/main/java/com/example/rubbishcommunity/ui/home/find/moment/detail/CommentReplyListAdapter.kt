package com.example.rubbishcommunity.ui.home.find.moment.detail

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CellCommentReplyBinding
import com.example.rubbishcommunity.model.api.moments.CommentReply
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class CommentReplyListAdapter(
	list: List<CommentReply>
) : BaseRecyclerAdapter<CommentReply, CellCommentReplyBinding>(
	R.layout.cell_comment_reply,
	{
		
	},
	baseList = list
) {
	override fun bindData(binding: CellCommentReplyBinding, position: Int) {
		binding.commentReply = baseList[position]
	}
}