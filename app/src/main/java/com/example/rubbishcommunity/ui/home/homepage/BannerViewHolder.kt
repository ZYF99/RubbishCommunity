package com.example.rubbishcommunity.ui.home.homepage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.PhotographyCellBinding
import com.example.rubbishcommunity.model.api.news.NewsResult
import com.zhouwei.mzbanner.holder.MZViewHolder

class BannerViewHolder(
	val list: List<NewsResult.News>
) : MZViewHolder<NewsResult.News> {
	
	private lateinit var photographyBinding: PhotographyCellBinding
	
	override fun createView(context: Context): View {
		photographyBinding = DataBindingUtil.inflate(
			LayoutInflater.from(context),
			R.layout.cell_photography, null, false
		)
		
		return photographyBinding.root
	}
	
	override fun onBind(p0: Context?, p1: Int, data: NewsResult.News?) {
		// 数据绑定
		photographyBinding.news = data
	}
}
