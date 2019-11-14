package com.example.rubbishcommunity.ui.home.search.newsdetail


import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.NewsDetailBinding


class NewsDetailFragment : BindingFragment<NewsDetailBinding, NewsDetailViewModel>(
	NewsDetailViewModel::class.java, R.layout.fragment_news_detail
) {
	
	
	override fun initBefore() {
		viewModel.newsUrl.value = activity!!.intent.getStringExtra("newsUrl")
		
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		binding.btnBack.setOnClickListener {
			activity?.finish()
		}
	}
	
	override fun initData() {
		binding.webView.loadUrl(viewModel.newsUrl.value)
	}
	
	
}