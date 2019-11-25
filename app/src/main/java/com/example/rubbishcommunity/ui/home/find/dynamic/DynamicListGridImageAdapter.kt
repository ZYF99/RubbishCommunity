package com.example.rubbishcommunity.ui.home.find.dynamic


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicGridItemBinding
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class DynamicListGridImageAdapter(
	val list: MutableList<String>,
	onCellClick: (Int) -> Unit
) :BaseRecyclerAdapter<String,DynamicGridItemBinding>(
	R.layout.item_dynamic_grid_image,
	onCellClick
) {
	override val baseList: MutableList<String>
		get() = list
	
	override fun bindData(binding: DynamicGridItemBinding, position: Int) {
		binding.imgUrl = list[position]
	}
}



	







