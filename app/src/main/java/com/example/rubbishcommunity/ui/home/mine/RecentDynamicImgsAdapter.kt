package com.example.rubbishcommunity.ui.home.mine

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ItemImageViewBinding
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class RecentDynamicImgsAdapter:BaseRecyclerAdapter<String, ItemImageViewBinding>(
	R.layout.item_image_view,
	{}
){
	override fun bindData(binding: ItemImageViewBinding, position: Int) {
		binding.imgUrl = baseList[position]
	}
	
}