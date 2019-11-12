package com.example.rubbishcommunity.ui.home.find.dynamic


import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicGridItemBinding


class DynamicListGridImageAdapter(
	private val list: List<String>,
	private val onItemClick: (Int) -> Unit
) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_dynamic_grid_image, list) {
	override fun convert(helper: BaseViewHolder, item: String) {
		val binding: DynamicGridItemBinding = DataBindingUtil.bind(helper.itemView)!!
		
		val options = RequestOptions()
			.centerCrop()
			.diskCacheStrategy(DiskCacheStrategy.ALL)
		
		Glide.with(mContext)
			.load(list[helper.position])
			.placeholder(R.color.colorPlaceHolder)
			.apply(options)
			.into(binding.girdImg)
		
		//itemView 的点击事件
		binding.root.setOnClickListener {
			onItemClick.invoke(helper.adapterPosition)
		}
		
	}
	
	
}


	







