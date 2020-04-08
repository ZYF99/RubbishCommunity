package com.example.rubbishcommunity.ui.home.homepage


import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.HomePageBinding
import com.example.rubbishcommunity.ui.container.jumpToNewsDetail
import com.example.rubbishcommunity.ui.container.jumpToSearch
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout


class HomePageFragment : BindingFragment<HomePageBinding, HomePageViewModel>(
	HomePageViewModel::class.java, R.layout.fragment_home_page
) {
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	}
	
	override fun onSoftKeyboardClosed() {
	}
	
	override fun initBefore() {
		binding.vm = viewModel
	}
	
	override fun initWidget() {
		
		binding.toolBar.setExpandedTitleColor(resources.getColor(R.color.transparent))
		
		//刷新状态监听
		viewModel.isRefreshing.observeNonNull {
			binding.swipe.isRefreshing = it
			binding.root.isEnabled = !it
		}
		
		viewModel.newsList.observeNonNull {
			(binding.recyclerNews.adapter as NewsListAdapter).replaceData(it)
		}
		
		viewModel.photographyList.observeNonNull {
			binding.banner.run {
				pause()
				setPages(it.toList()) { BannerViewHolder(it) }
				start()
			}
		}

		
		binding.recyclerNews.apply {
			adapter = NewsListAdapter(
				viewModel.newsList.value!!
			) { news ->
				jumpToNewsDetail(context, news.url)
			}
		}
		
		RxSwipeRefreshLayout.refreshes(binding.swipe).doOnNext {
			refresh()
		}.bindLife()
		
		
	}
	
	override fun initData() {
		refresh()
	}
	
	private fun refresh() {
		
		context!!.checkNet().doOnComplete {
			viewModel.getNews()
			viewModel.getPhotography()
		}.doOnError {
			viewModel.isRefreshing.postValue(false)
		}.bindLife()
		
	}
	
	override fun onResume() {
		super.onResume()
		binding.banner.start()
	}
	
	override fun onPause() {
		super.onPause()
		binding.banner.pause()
	}
}



