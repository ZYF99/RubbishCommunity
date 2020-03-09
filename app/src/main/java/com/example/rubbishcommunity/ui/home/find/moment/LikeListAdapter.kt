package com.example.rubbishcommunity.ui.home.find.moment


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CellCircleImgBinding
import com.example.rubbishcommunity.model.api.SimpleProfileResp
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class LikeListAdapter(
	val list:List<SimpleProfileResp>,
	val onCellClick: (Int) -> Unit
) :BaseRecyclerAdapter<SimpleProfileResp,CellCircleImgBinding>(
	R.layout.cell_circle_img,
	{},
	baseList = list
) {
	override fun bindData(binding: CellCircleImgBinding, position: Int) {
		binding.liker = baseList[position]
		binding.root.setOnClickListener {
			onCellClick(position)
		}
	}
}



	







