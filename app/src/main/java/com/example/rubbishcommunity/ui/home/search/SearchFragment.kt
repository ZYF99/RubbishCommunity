package com.example.rubbishcommunity.ui.home.search

import android.view.inputmethod.EditorInfo
import android.widget.TextView.*
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SearchBinding
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
		binding.searchEdit.setOnEditorActionListener(OnEditorActionListener { p0, p1, p2 ->
			when (p1) {
				//点击Search
				EditorInfo.IME_ACTION_SEARCH -> {
					analysisAndSearch(viewModel.searchWord.value!!)
					true
				}
				else -> false
			}
		})
		
		
		binding.gridHotword.run {
			val hotWordList = listOf(
				"苹果", "苹果", "苹果", "苹果",
				"苹果", "苹果", "苹果", "苹果",
				"苹果", "苹果", "苹果", "苹果"
			)
			adapter = TagGridAdapter(context, hotWordList)
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
				hideKeyboard()
			}.bindLife()
	}
	
}