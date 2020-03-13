package com.example.rubbishcommunity.ui.home.find.moment.detail

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CommentCellBinding
import com.example.rubbishcommunity.model.api.moments.MomentComment
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class CommentListAdapter(
	private val onReplyClick: (Int) -> Unit,
	list: List<MomentComment>
) : BaseRecyclerAdapter<MomentComment, CommentCellBinding>(
	R.layout.cell_comment,
	{
		
	},
	baseList = list
) {
	
	override fun bindData(binding: CommentCellBinding, position: Int) {
		val comment = baseList[position]
		binding.comment = comment
		//回复按钮
		binding.btnReply.setOnClickListener {
			onReplyClick(position)
		}
		//评论回复的列表
		binding.recCommentReply.run {
			layoutManager = LinearLayoutManager(context)
			adapter =
				CommentReplyListAdapter(
					comment.commentReplyList
				)
		}
	}
	
}