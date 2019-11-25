package com.example.rubbishcommunity.ui.home.homepage.search


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SearchCellBinding
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter



class SearchListAdapter(
	val list: MutableList<SearchKeyConclusion>,
	onCellClick: (Int) -> Unit
) : BaseRecyclerAdapter<SearchKeyConclusion, SearchCellBinding>(
	R.layout.cell_search,
	onCellClick
) {
	override val baseList: MutableList<SearchKeyConclusion>
		get() = list
	
	override fun bindData(binding: SearchCellBinding, position: Int) {
		binding.searchConclusion = baseList[position]
	}
}
