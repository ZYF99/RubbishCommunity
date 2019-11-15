package com.example.rubbishcommunity.ui.home.homepage.newsdetail


import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.NewsDetailBinding
import com.zzhoujay.richtext.RichText


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
		RichText.initCacheDir(context)
	}
	
	override fun initData() {
		binding.webView.loadUrl(viewModel.newsUrl.value)
		
		
/*		RichText.from(mockRichText()).bind(this)
			.showBorder(true)
			.size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
			.into(binding.richText)*/
	}
	
	
}