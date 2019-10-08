package com.example.rubbishcommunity.ui.home.search

import android.annotation.SuppressLint
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
	
	
	@SuppressLint("CheckResult")
	override fun initWidget() {
		binding.vm = viewModel
		//viewModel.isRefreshing.observe { binding.refreshlayout.isRefreshing = it!! }
		viewModel.init()
		binding.searchEdit.setOnEditorActionListener(OnEditorActionListener { p0, p1, p2 ->
			when (p1) {
				//点击Search
				EditorInfo.IME_ACTION_SEARCH -> {
					Single.just(viewModel.searchWord.value!!).flatMap {
						Single.just(HanLP.extractSummary(it, 10))
					}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
						.doOnSuccess {
							MyApplication.showToast(it.toString())
						}.bindLife()
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
	
	
}