package com.example.rubbishcommunity.ui.home.find.dynamic.detail



import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CommentCellBinding
import com.example.rubbishcommunity.model.Comment



class CommentListAdapter(data: List<Comment>?, private val onClickListener:OnClickListener) :
	BaseQuickAdapter<Comment, BaseViewHolder>(R.layout.cell_comment, data) {
	
	override fun convert(helper: BaseViewHolder, comment: Comment) {
		val binding: CommentCellBinding = DataBindingUtil.bind(helper.itemView)!!
		binding.comment = comment
		//查看内部评论按钮
		binding.btnLookcomment.setOnClickListener {
			onClickListener.onLookCommentClick(helper.position)
		}
		//回复按钮
		binding.btnReply.setOnClickListener {
			onClickListener.onoReplyClick(helper.position)
		}
	}
	
	interface OnClickListener{
		fun onLookCommentClick(position:Int)
		fun onoReplyClick(position:Int)
		
	}

}