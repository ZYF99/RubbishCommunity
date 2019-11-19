package com.example.rubbishcommunity.ui.home.homepage



import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.HomePageBinding
import com.example.rubbishcommunity.model.Photography
import com.example.rubbishcommunity.ui.container.jumpToNewsDetail
import com.hankcs.hanlp.HanLP
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class HomePageFragment : BindingFragment<HomePageBinding, HomePageViewModel>(
	HomePageViewModel::class.java, R.layout.fragment_search
) {
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	}
	
	override fun onSoftKeyboardClosed() {
	}
	
	override fun initBefore() {
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		viewModel.init()
		
		//刷新状态监听
		viewModel.isRefreshing.observeNonNull {
			binding.swipe.isRefreshing = it
			binding.root.isEnabled = !it
		}
		
		viewModel.newsList.observeNonNull {
			(binding.recyclerNews.adapter as NewsListAdapter).replaceData(it)
		}
		
		binding.recyclerImg.apply {
			layoutManager = LinearLayoutManager(context).apply {
				orientation = LinearLayoutManager.HORIZONTAL
			}
			adapter = PhotographyListAdapter(
				context,
				listOf(
					Photography(
						"",
						"",
						"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573557155327&di=44f5ec8ef0f6fcbbf28131b3f9af5df5&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2F20171020%2F68fc-fymzzpv8123943.jpg"
					),
					Photography(
						"",
						"",
						"http://img1.imgtn.bdimg.com/it/u=3053304738,3863903291&fm=26&gp=0.jpg"
					),
					Photography(
						"",
						"",
						"http://img3.imgtn.bdimg.com/it/u=3774166848,2531253577&fm=26&gp=0.jpg"
					)
				)
			)
			
		}
		binding.recyclerNews.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = NewsListAdapter(
				viewModel.newsList.value ?: mutableListOf()
			) { news ->
				jumpToNewsDetail(context, news.url)
			}
			
		}
		
		binding.linearSearch.setOnClickListener {
		viewModel.protobufTest()
		}
		
		RxSwipeRefreshLayout.refreshes(binding.swipe).doOnNext {
			viewModel.getNews()
		}.bindLife()
		
		
	}
	
	override fun initData() {
		viewModel.getNews()
	}
	
	private fun analysisAndSearch(str: String) {
		Single.just(str).flatMap {
			Single.just(HanLP.extractSummary(it, 10))
		}.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.flatMap {
				MyApplication.showSuccess("解析结果：$it")
				viewModel.search(it[0])
			}.doOnSuccess {
				MyApplication.showSuccess(it.data.toString())
			}.bindLife()
	}
	
	
	
	
}

