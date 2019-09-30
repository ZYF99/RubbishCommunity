package com.example.rubbishcommunity.ui.home.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.daimajia.swipe.SimpleSwipeListener
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MsgCellBinding
import com.example.rubbishcommunity.model.Message
import com.jakewharton.rxbinding2.view.RxView

import java.util.ArrayList
import java.util.concurrent.TimeUnit

class RecyclerViewAdapter
//protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);
	
	(private val mContext: Context, private val mDataset: MutableList<Message>) :
	RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder>() {
	override fun getSwipeLayoutResourceId(position: Int): Int {
		return R.id.swipe
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
		
		mItemManger.bindView(binding.root, position)
		RxView.clicks(binding.root).throttleFirst(2, TimeUnit.SECONDS).doOnNext {
			MyApplication.showToast("AAAAAA")
			mItemManger.closeAllItems()
		}.subscribe()
		
		binding.swipe.showMode = SwipeLayout.ShowMode.LayDown;
        binding.delete.setOnClickListener {
			mItemManger.removeShownLayouts(binding.swipe)
			mDataset.removeAt(position);
			notifyItemRemoved(position);
			notifyItemRangeChanged(position, mDataset.size)
			mItemManger.closeAllItems();
		}
		binding.cellPersonName.text = msg.msg

	}
	
	override fun getItemCount(): Int {
		return mDataset.size
	}
	
}