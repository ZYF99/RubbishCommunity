package com.example.rubbishcommunity.ui.home.message


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MsgCellBinding
import com.example.rubbishcommunity.model.Message
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit


class MessageListAdapter(
	val list: MutableList<Message>,
	onCellClick: (Int) -> Unit,
	val onDellClick: (Int) -> Unit
) : BaseRecyclerAdapter<Message, MsgCellBinding>(
	R.layout.cell_msg,
	onCellClick
) {
	override val baseList: MutableList<Message>
		get() = list
	
	override fun bindData(binding: MsgCellBinding, position: Int) {
		binding.message = list[position]
		RxView.clicks(binding.delete).throttleFirst(2, TimeUnit.SECONDS).doOnNext {
			removeData(position)
			onDellClick(position)
		}.subscribe()
	}
}

