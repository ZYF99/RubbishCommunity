package com.example.rubbishcommunity.ui.home.homepage


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.PhotographyCellBinding
import com.example.rubbishcommunity.model.Photography

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/9/28 21:10
 */

class PhotographyListAdapter
	(
	private val dataList: MutableList<Photography>
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
	
	fun replaceData(newList: MutableList<Photography>) {
		dataList.clear()
		dataList.addAll(newList)
		notifyDataSetChanged()
	}
	
}