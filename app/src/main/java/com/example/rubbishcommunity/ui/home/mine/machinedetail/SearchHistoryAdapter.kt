package com.example.rubbishcommunity.ui.home.mine.machinedetail

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CellMachineSearchHistoryBinding
import com.example.rubbishcommunity.model.api.machine.FetchMachineSearchHistoryResultModel
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class SearchHistoryAdapter(
	onCellClick: (FetchMachineSearchHistoryResultModel.MachineSearchHistory.SearchCount) -> Unit
) :
	BaseRecyclerAdapter<FetchMachineSearchHistoryResultModel.MachineSearchHistory.SearchCount, CellMachineSearchHistoryBinding>(
		R.layout.cell_machine_search_history,
		onCellClick
	) {
	override fun bindData(binding: CellMachineSearchHistoryBinding, position: Int) {
		binding.searchModel = baseList[position]
	}
	
	
}