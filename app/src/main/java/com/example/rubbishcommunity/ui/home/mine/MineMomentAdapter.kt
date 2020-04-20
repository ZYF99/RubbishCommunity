package com.example.rubbishcommunity.ui.home.mine

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ItemRecentMomentBinding
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class MineMomentAdapter(
	onCellClick: (MomentContent) -> Unit
) : BaseRecyclerAdapter<MomentContent, ItemRecentMomentBinding>(
	R.layout.item_recent_moment,
	onCellClick
) {
	override fun bindData(binding: ItemRecentMomentBinding, position: Int) {
		binding.moment = baseList[position]
	}
	
}