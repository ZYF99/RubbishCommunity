package com.example.rubbishcommunity.ui.home.message.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ChatMsgCellBinding
import com.example.rubbishcommunity.model.ChatMessage
import com.example.rubbishcommunity.persistence.getLocalOpenId
import com.example.rubbishcommunity.persistence.getLocalUserInfo
import java.util.*

class ChatListAdapter
	(
	private val mContext: Context,
	private val mDataset: MutableList<ChatMessage>
) : RecyclerView.Adapter<ChatListAdapter.SimpleViewHolder>() {
	
	lateinit var binding: ChatMsgCellBinding
	var lastMsg: ChatMessage? = null
	
	class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
		val view =
			LayoutInflater.from(parent.context).inflate(R.layout.cell_chat_msg, parent, false)
		return SimpleViewHolder(view)
	}
	
	override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {
		binding = DataBindingUtil.bind(viewHolder.itemView)!!
		val msg = mDataset[position]
		binding.chatMessage = msg
		
		
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
			binding.rightcard.visibility = View.VISIBLE
			binding.leftcard.visibility = View.GONE
			binding.rightimg.visibility = View.VISIBLE
			binding.leftimg.visibility = View.GONE
			
			
		} else {
			//对方的消息
			binding.rightcard.visibility = View.GONE
			binding.leftcard.visibility = View.VISIBLE
			binding.rightimg.visibility = View.GONE
			binding.leftimg.visibility = View.VISIBLE
			
		}
		
		lastMsg = msg
	}
	
	override fun getItemCount(): Int {
		return mDataset.size
	}
	
}