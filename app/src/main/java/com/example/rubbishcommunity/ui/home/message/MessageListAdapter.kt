package com.example.rubbishcommunity.ui.home.message


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MsgCellBinding
import com.example.rubbishcommunity.model.Message
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter


class MessageListAdapter(
	val list: MutableList<Message>,
	val onCellClick: (Int) -> Unit,
	val onDellClick: (Int) -> Unit
) : BaseRecyclerAdapter<Message, MsgCellBinding>(
	R.layout.cell_msg,
	onCellClick
) {
	override val baseList: MutableList<Message>
		get() = list
	
	override fun bindData(binding: MsgCellBinding, position: Int) {
		binding.message = list[position]
		
		binding.cell.setOnClickListener {
			onCellClick(position)
		}
		
		binding.delete.setOnClickListener {
			removeData(position)
			onDellClick(position)
		}
	}
}

