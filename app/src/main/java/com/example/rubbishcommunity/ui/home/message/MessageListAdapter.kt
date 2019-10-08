package com.example.rubbishcommunity.ui.home.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MsgCellBinding
import com.example.rubbishcommunity.model.Message
import com.jakewharton.rxbinding2.view.RxView

import java.util.concurrent.TimeUnit

class MessageListAdapter
	(
	private val mContext: Context,
	private val mDataset: MutableList<Message>,
	private val cellClickListener: OnClickListener
) : RecyclerView.Adapter<MessageListAdapter.SimpleViewHolder>() {
	interface OnClickListener {
		fun onCellClick(position: Int)
		fun onDellClick(position: Int)
	}
	
	
	lateinit var binding: MsgCellBinding
	
	class SimpleViewHolder
		(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_msg, parent, false)
		return SimpleViewHolder(view)
	}
	
	override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {
		binding = DataBindingUtil.bind(viewHolder.itemView)!!
		val msg = mDataset[position]
		
		RxView.clicks(binding.cell).throttleFirst(2, TimeUnit.SECONDS).doOnNext {
			cellClickListener.onCellClick(position)
		}.subscribe()
		
		RxView.clicks(binding.delete).throttleFirst(2, TimeUnit.SECONDS).doOnNext {
			mDataset.removeAt(position)
			notifyItemRemoved(position)
			notifyItemRangeChanged(position, mDataset.size)
			cellClickListener.onDellClick(position)
		}.subscribe()
		
		binding.cellPersonName.text = msg.msg
	}
	
	override fun getItemCount(): Int {
		return mDataset.size
	}
	
}