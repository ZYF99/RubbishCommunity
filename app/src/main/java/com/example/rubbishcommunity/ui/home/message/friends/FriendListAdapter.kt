package com.example.rubbishcommunity.ui.home.message.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FriendListCellBinding
import com.example.rubbishcommunity.model.Person
import com.jakewharton.rxbinding2.view.RxView

import java.util.concurrent.TimeUnit

class FriendListAdapter
	(
	private val mContext: Context,
	private val mDataset: MutableList<Person>,
	private val cellClickListener: OnClickListener
) : RecyclerView.Adapter<FriendListAdapter.SimpleViewHolder>() {
	interface OnClickListener {
		fun onCellClick(position: Int)
	}
	
	
	class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_friend, parent, false)
		return SimpleViewHolder(view)
	}
	
	override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {
		val binding: FriendListCellBinding = DataBindingUtil.bind(viewHolder.itemView)!!
		
		binding.person = mDataset[position]
		
		RxView.clicks(binding.cell).throttleFirst(2, TimeUnit.SECONDS).doOnNext {
			cellClickListener.onCellClick(position)
		}.subscribe()
		
	}
	
	override fun getItemCount(): Int {
		return mDataset.size
	}
	
}