package com.example.rubbishcommunity.ui.home.find.moment


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicGridItemBinding
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class MomentsListGridImageAdapter(
	val list:List<String>,
	val onCellClick: (Int) -> Unit
) :BaseRecyclerAdapter<String,DynamicGridItemBinding>(
	R.layout.item_dynamic_grid_image,
	{},
	list
) {
	override fun bindData(binding: DynamicGridItemBinding, position: Int) {
		binding.imgUrl = baseList[position]
		binding.root.setOnClickListener {
			onCellClick(position)
		}
	}
}



	







