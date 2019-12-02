package com.example.rubbishcommunity.ui.home.homepage.search.cameraSearch


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ThingCellBinding
import com.example.rubbishcommunity.model.api.baiduidentify.Thing
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter


class ThingListAdapter(
	val list: MutableList<Thing>,
	onCellClick: (Int) -> Unit
) : BaseRecyclerAdapter<Thing, ThingCellBinding>(
	R.layout.cell_thing,
	onCellClick
) {
	override val baseList: MutableList<Thing>
		get() = list
	
	override fun bindData(binding: ThingCellBinding, position: Int) {
		binding.thing = baseList[position]
	}
}
