package com.example.rubbishcommunity.ui.home.homepage.newsdetail

import android.net.http.SslError
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.NewsDetailBinding
import com.example.rubbishcommunity.model.api.news.NewsResult
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.utils.showGallery
import com.example.rubbishcommunity.utils.globalGson
import com.zzhoujay.richtext.RichText
import java.net.URLEncoder

class NewsDetailFragment : BindingFragment<NewsDetailBinding, NewsDetailViewModel>(
	NewsDetailViewModel::class.java, R.layout.fragment_news_detail
) {
	
	
	override fun initBefore() {
		viewModel.news.value = globalGson.fromJson<NewsResult.News>(
			activity!!.intent.getStringExtra(
				"news"
			), NewsResult.News::class.java
		)
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		binding.webView.webViewClient = object : WebViewClient() {
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
		binding.btnBack.setOnClickListener { activity?.finish() }
		binding.banner.run {
			val imgs = viewModel.news.value?.frontCoverImages ?: emptyList()
			setPages(imgs) {
				NewsImageBannerViewHolder(imgs)
			}
		}
	}
	
	override fun initData() {
		val news = viewModel.news.value
		//加载新闻
		when {
			//HTML的url
			news?.isURL ?: false -> {
				binding.webView.visibility = View.VISIBLE
				binding.tvRichText.visibility = View.GONE
				binding.webView.loadUrl(news?.payload)
			}
			//文字
			(news?.isTEXT ?: false) || news?.isHTML ?: false -> {
				binding.webView.visibility = View.VISIBLE
				binding.tvRichText.visibility = View.GONE
				binding.webView.loadData(
					URLEncoder.encode(news?.payload, "utf-8"),
					"text/html",
					"utf-8"
				)
			}
			//MD文档
			news?.isMD ?: false -> {
				binding.webView.visibility = View.GONE
				binding.tvRichText.visibility = View.VISIBLE
				RichText.fromMarkdown(news?.payload)
					.imageClick { imageUrls, position ->
						showGallery(context!!, imageUrls, position)
					}
					.into(binding.tvRichText)
			}
		}
	}
	
	override fun onDestroy() {
		RichText.clear(activity)
		super.onDestroy()
	}
	
}