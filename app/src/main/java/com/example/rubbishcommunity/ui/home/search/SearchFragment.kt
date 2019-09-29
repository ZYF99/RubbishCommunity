package com.example.rubbishcommunity.ui.home.search

import android.annotation.SuppressLint
import android.view.inputmethod.EditorInfo
import android.widget.TextView.*
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SearchBinding


class SearchFragment : BindingFragment<SearchBinding, SearchViewModel>(
	SearchViewModel::class.java, R.layout.fragment_search
) {
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
					MyApplication.showToast(viewModel.searchWord.value!!)
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