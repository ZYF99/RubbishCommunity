package com.example.rubbishcommunity.ui.home.mine

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.HeaderMineBinding
import com.example.rubbishcommunity.databinding.ItemRecentMomentBinding
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.ui.adapter.HeaderRecyclerAdapter

class MineMomentAdapter(
	headerBinding: HeaderMineBinding,
	onCellClick: (MomentContent) -> Unit
) : HeaderRecyclerAdapter<MomentContent, ItemRecentMomentBinding, HeaderMineBinding>(
	R.layout.item_recent_moment,
	onCellClick
) {
	override fun bindData(binding: ItemRecentMomentBinding?, position: Int) {
		binding?.moment = baseList[position]
	}
	
	override fun onHeaderInit() {
	
	}

}