package com.example.rubbishcommunity.ui.home.message

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MsgCellBinding
import com.example.rubbishcommunity.model.Message
import com.example.rubbishcommunity.ui.adapter.EmptyRecyclerAdapter

class MessageListAdapter(
	val onCellClick: (Message) -> Unit,
	val onDellClick: (Int) -> Unit
) : EmptyRecyclerAdapter<Message, MsgCellBinding>(
	R.layout.cell_msg,
	onCellClick
) {
	override fun bindData(binding: MsgCellBinding, position: Int) {
		binding.message = baseList[position]
		
		binding.cell.setOnClickListener {
			onCellClick(baseList[position])
		}
		
		binding.delete.setOnClickListener {
			replaceData(baseList.apply { toMutableList().removeAt(position) })
			onDellClick(position)
		}
	}
}

