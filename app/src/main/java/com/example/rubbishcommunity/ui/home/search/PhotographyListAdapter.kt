package com.example.rubbishcommunity.ui.home.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.PhotographyCellBinding
import com.example.rubbishcommunity.model.Photography



class PhotographyListAdapter
	(
	private val mContext: Context,
	private val dataList: List<Photography>
) : RecyclerView.Adapter<PhotographyListAdapter.SimpleViewHolder>() {
	

	lateinit var binding: PhotographyCellBinding
	
	class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_photography, parent, false)
		return SimpleViewHolder(view)
	}
	
	override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {
		binding = DataBindingUtil.bind(viewHolder.itemView)!!
		binding.photography = dataList[position]
		
	}
	
	override fun getItemCount(): Int {
		return dataList.size
	}
	
}