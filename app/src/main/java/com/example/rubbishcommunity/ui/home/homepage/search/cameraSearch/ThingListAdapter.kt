package com.example.rubbishcommunity.ui.home.homepage.search.cameraSearch


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ThingCellBinding
import com.example.rubbishcommunity.model.api.baiduidentify.Thing
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter


class ThingListAdapter(
	onCellClick: (Thing) -> Unit
) : BaseRecyclerAdapter<Thing, ThingCellBinding>(
	R.layout.cell_thing,
	onCellClick
) {
	
	override fun bindData(binding: ThingCellBinding, position: Int) {
		binding.thing = baseList[position]
	}
}
