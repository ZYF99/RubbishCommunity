package com.example.rubbishcommunity.ui.home.homepage


import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.HomePageBinding
import com.example.rubbishcommunity.ui.container.jumpToNewsDetail
import com.example.rubbishcommunity.ui.container.jumpToSearch
import com.hankcs.hanlp.HanLP
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


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
		
		//刷新状态监听
		viewModel.isRefreshing.observeNonNull {
			binding.swipe.isRefreshing = it
			binding.root.isEnabled = !it
		}
		
		viewModel.newsList.observeNonNull {
			(binding.recyclerNews.adapter as NewsListAdapter).replaceData(it)
		}
		
		viewModel.photographyList.observeNonNull {
			(binding.recyclerPhotography.adapter as PhotographyListAdapter).replaceData(it)
		}
		
		binding.recyclerPhotography.apply {
			layoutManager = LinearLayoutManager(context).apply {
				orientation = LinearLayoutManager.HORIZONTAL
			}
			adapter = PhotographyListAdapter(
				viewModel.photographyList.value!!
			)
			
		}
		binding.recyclerNews.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = NewsListAdapter(
				viewModel.newsList.value!!
			) { news ->
				jumpToNewsDetail(context, news.url)
			}
		}
		
		binding.linearSearch.setOnClickListener {
			jumpToSearch(context!!)
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
	
	
}

