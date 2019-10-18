package com.example.rubbishcommunity.ui.home.find.dynamic


import android.view.View
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicListCellBinding
import com.example.rubbishcommunity.model.Dynamic
import com.example.rubbishcommunity.ui.release.dynamic.MyGridLayoutManager
import com.example.rubbishcommunity.ui.showGallery


class DynamicListAdapter(data: MutableList<Dynamic>?) :
	BaseQuickAdapter<Dynamic, BaseViewHolder>(R.layout.cell_dynamic, data) {
	
	override fun convert(helper: BaseViewHolder, dynamic: Dynamic) {
		val binding: DynamicListCellBinding = DataBindingUtil.bind(helper.itemView)!!
		binding.dynamic = dynamic
		binding.recImg.run {
			layoutManager = MyGridLayoutManager(
				context,
				when {
					dynamic.images.size % 3 == 0 -> 3
					dynamic.images.size == 1 -> 1
					dynamic.images.size == 2 -> 2
					dynamic.images.size == 4 -> 2
					else -> {
						3
					}
				}
			)
			
			
			adapter = DynamicListGridImageAdapter(
				dynamic.images,
				object : DynamicListGridImageAdapter.OnItemClickListener {
					override fun onItemClick(position: Int, v: View) {
						showGallery(context, dynamic.images, position)
					}
				}
			)
		}
	}
	
	
}