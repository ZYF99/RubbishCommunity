package com.example.rubbishcommunity.ui.home.homepage.newsdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.BannerImageBinding
import com.zhouwei.mzbanner.holder.MZViewHolder

class NewsImageBannerViewHolder(
	val list: List<String>
) : MZViewHolder<String> {
	
	private lateinit var imgBinding: BannerImageBinding
	
	override fun createView(context: Context): View {
		imgBinding = DataBindingUtil.inflate(
			LayoutInflater.from(context),
			R.layout.banner_image, null, false
		)
		return imgBinding.root
	}
	
	override fun onBind(p0: Context?, p1: Int, data: String?) {
		// 数据绑定
		imgBinding.url = data
	}
}
