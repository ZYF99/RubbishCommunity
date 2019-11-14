package com.example.rubbishcommunity.ui.home.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.NewsCellBinding
import com.example.rubbishcommunity.model.api.News


class NewsListAdapter
	(
	private val dataList: MutableList<News>,
	private val onCellClick: (News)->Unit
) : RecyclerView.Adapter<NewsListAdapter.SimpleViewHolder>() {
	
	
	lateinit var binding: NewsCellBinding
	
	class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_news, parent, false)
		return SimpleViewHolder(view)
	}
	
	override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {
		binding = DataBindingUtil.bind(viewHolder.itemView)!!
		binding.news = dataList[position]
		binding.cell.setOnClickListener {
			onCellClick(dataList[position])
		}
		
	}
	
	override fun getItemCount(): Int {
		return dataList.size
	}
	
	fun replaceData(datas: MutableList<News>) {
		if (dataList != datas) {
			dataList.clear()
			dataList.addAll(datas)
		}
		notifyDataSetChanged()
	}
	
	
	
}