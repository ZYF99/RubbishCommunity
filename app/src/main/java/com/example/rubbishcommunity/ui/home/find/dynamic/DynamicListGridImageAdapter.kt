package com.example.rubbishcommunity.ui.home.find.dynamic


import android.view.View
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
	private val mOnItemClickListener: OnItemClickListener
) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_dynamic_grid_image, list) {
	
	
	interface OnItemClickListener {
		fun onItemClick(item: String, v: View)
	}
	
	override fun convert(helper: BaseViewHolder, item: String) {
		val binding: DynamicGridItemBinding = DataBindingUtil.bind(helper.itemView)!!
		
		val options = RequestOptions()
			.centerCrop()
			.placeholder(R.color.bar_grey_90)
			.diskCacheStrategy(DiskCacheStrategy.ALL)
		Glide.with(mContext)
			.load(item)
			.apply(options)
			.into(binding.girdImg)
		
		//itemView 的点击事件
		binding.root.setOnClickListener { v ->
			mOnItemClickListener.onItemClick(item, v)
		}
		
	}
	
	
}


	







