package com.example.rubbishcommunity.ui.home.message.chat



import android.view.View
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ChatMsgCellBinding
import com.example.rubbishcommunity.model.ChatMessage
import com.example.rubbishcommunity.persistence.getLocalOpenId
import com.example.rubbishcommunity.persistence.getLocalUserInfo
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter
import java.util.*


class ChatListAdapter(
	val list: MutableList<ChatMessage>,
	onCellClick: (Int) -> Unit
) : BaseRecyclerAdapter<ChatMessage, ChatMsgCellBinding>(
	R.layout.cell_chat_msg,
	onCellClick
) {
	override val baseList: MutableList<ChatMessage>
		get() = list
	private var lastMsg: ChatMessage? = null
	
	override fun bindData(binding: ChatMsgCellBinding, position: Int) {
		val msg = list[position]
		binding.chatMessage = msg
		binding.localOpenId = getLocalOpenId()
		binding.cellMsgTime.visibility = View.VISIBLE
		
		if (lastMsg != null) {
			if (lastMsg!!.openId == getLocalOpenId()) {
				//上一条是我自己发的消息
				if (Date(msg.time).minutes == Date(lastMsg!!.time).minutes) {
					//这是一分钟之内我继续发的消息
					binding.cellMsgTime.visibility = View.GONE
				}
			}
		}
		
		if (msg.openId == getLocalOpenId()) {
			//自己发送的消息
			binding.myAvatar = getLocalUserInfo().avatar
		} else {
			//对方的消息
		}
		lastMsg = msg
	}
}
