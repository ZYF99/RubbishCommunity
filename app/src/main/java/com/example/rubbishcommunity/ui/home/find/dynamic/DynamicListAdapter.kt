package com.example.rubbishcommunity.ui.home.find.dynamic


import androidx.recyclerview.widget.GridLayoutManager
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicListCellBinding
import com.example.rubbishcommunity.model.Dynamic
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter
import com.example.rubbishcommunity.ui.showGallery


class DynamicListAdapter(
	val list: MutableList<Dynamic>,
	onCellClick: (Int) -> Unit
) : BaseRecyclerAdapter<Dynamic, DynamicListCellBinding>(
	R.layout.cell_dynamic,
	onCellClick
) {
	override val baseList: MutableList<Dynamic>
		get() = list
	
	override fun bindData(binding: DynamicListCellBinding, position: Int) {
		binding.dynamic = list[position]
		binding.recImg.run {
			layoutManager = GridLayoutManager(
				context,
				when {
					list[position].images.size % 3 == 0 -> 3
					list[position].images.size == 1 -> 1
					list[position].images.size == 2 -> 2
					list[position].images.size == 4 -> 2
					else -> 3
				}
			)
			adapter = DynamicListGridImageAdapter(
				list[position].images
			) { position ->
				showGallery(context, list[position].images, position)
			}
		}
	}
}

