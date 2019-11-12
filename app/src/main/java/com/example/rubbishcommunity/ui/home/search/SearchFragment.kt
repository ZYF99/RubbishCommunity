package com.example.rubbishcommunity.ui.home.search


import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SearchBinding
import com.example.rubbishcommunity.model.Article
import com.example.rubbishcommunity.model.Photography
import com.example.rubbishcommunity.ui.utils.dp2px
import com.hankcs.hanlp.HanLP
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SearchFragment : BindingFragment<SearchBinding, SearchViewModel>(
	SearchViewModel::class.java, R.layout.fragment_search
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
		binding.recyclerArticle.apply {
			val item = Article(
				"",
				"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573557155327&di=44f5ec8ef0f6fcbbf28131b3f9af5df5&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2F20171020%2F68fc-fymzzpv8123943.jpg",
				"阿森松岛",
				"第三方的身份",
				""
			)
			
			layoutManager = LinearLayoutManager(context)
			adapter = ArticleListAdapter(
				context, listOf(
					item, item, item, item, item, item, item, item, item, item
				)
			)
			
		}
		
		binding.linearSearch.setOnClickListener {
			expandSearch()
		}
		
		binding.scrollview.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
			if (scrollY - oldScrollY > 0) reduceSearch()
			
		}
		
	}
	
	override fun initData() {
	
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
	
	
	private fun expandSearch() {
		//设置伸展状态时的布局
		binding.textSearch.hint = "搜索你的垃圾名称"
		val layoutParams = binding.linearSearch.layoutParams as ViewGroup.MarginLayoutParams
		layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
		context!!.run {
			//layoutParams.setMargins(dp2px(10f), dp2px(10f), dp2px(2f), dp2px(10f))
		}
		
		
		binding.linearSearch.layoutParams = layoutParams
		//设置动画
		beginDelayedTransition(binding.linearSearch)
	}
	
	private fun reduceSearch() {
		//设置收缩状态时的布局
		
		binding.textSearch.hint = ""
		val layoutParams = binding.linearSearch.layoutParams as ViewGroup.MarginLayoutParams
		
		context!!.run {
			layoutParams.width = dp2px(80f)
			//layoutParams.setMargins(dp2px(10f), dp2px(10f), dp2px(10f), dp2px(10f))
		}
		
		binding.linearSearch.layoutParams = layoutParams
		//设置动画
		beginDelayedTransition(binding.linearSearch)
	}
	
	private fun beginDelayedTransition(view: ViewGroup) {
		val mSet = AutoTransition()
		//设置动画持续时间
		mSet.duration = 300
		// 开始表演
		TransitionManager.beginDelayedTransition(view, mSet)
		
	}
	
}