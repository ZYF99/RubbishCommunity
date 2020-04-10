package com.example.rubbishcommunity.ui.home.homepage

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CellNewsBinding
import com.example.rubbishcommunity.model.api.news.NewsResult
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class NewsListAdapter(onCellClick: (NewsResult.News) -> Unit) :
	BaseRecyclerAdapter<NewsResult.News, CellNewsBinding>(
		R.layout.cell_news,
		onCellClick
	) {
	override fun bindData(binding: CellNewsBinding, position: Int) {
		binding.news = baseList[position]
	}
}