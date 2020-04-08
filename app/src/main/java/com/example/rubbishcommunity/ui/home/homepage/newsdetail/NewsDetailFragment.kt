package com.example.rubbishcommunity.ui.home.homepage.newsdetail

import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.NewsDetailBinding
import com.example.rubbishcommunity.ui.base.BindingFragment


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
		binding.webView.webViewClient = object :WebViewClient(){
			override fun onReceivedSslError(
				view: WebView?,
				handler: SslErrorHandler,
				error: SslError?
			) {
				handler.proceed()
				super.onReceivedSslError(view, handler, error)
			}
		}
		binding.webView.settings.run {
			javaScriptEnabled = true
			mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
		}
	}
	
	override fun initData() {

		//binding.webView.loadUrl(viewModel.newsUrl.value)
		binding.webView.loadUrl("https://github.com/CyC2018/CS-Notes/blob/master/notes/Java%20%E5%9F%BA%E7%A1%80.md")

	}
	
	
}