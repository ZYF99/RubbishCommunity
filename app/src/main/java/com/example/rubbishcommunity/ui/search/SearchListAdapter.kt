package com.example.rubbishcommunity.ui.search


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SearchCellBinding
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.example.rubbishcommunity.ui.adapter.EmptyRecyclerAdapter


class SearchListAdapter(
	val list: MutableList<SearchKeyConclusion>,
	onCellClick: (SearchKeyConclusion) -> Unit
) : EmptyRecyclerAdapter<SearchKeyConclusion, SearchCellBinding>(
	R.layout.cell_search,
	onCellClick
) {
	override fun bindData(binding: SearchCellBinding, position: Int) {
		binding.searchConclusion = baseList[position]
	}
}
