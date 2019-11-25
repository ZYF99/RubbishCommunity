package com.example.rubbishcommunity.ui.home.homepage.search


import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SearchBinding

import com.example.rubbishcommunity.ui.utils.hideSoftKeyBoard
import com.example.rubbishcommunity.ui.utils.openSoftKeyBoard


class SearchFragment : BindingFragment<SearchBinding, SearchViewModel>(
	SearchViewModel::class.java, R.layout.fragment_search
) {
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	}
	
	override fun onSoftKeyboardClosed() {
	}
	
	override fun initBefore() {}
	
	override fun initWidget() {
		binding.vm = viewModel
		
		
		//弹出软键盘
		activity!!.openSoftKeyBoard(binding.searchEdit)
		
		//软键盘状态监听
		viewModel.shouldShowInput.observeNonNull {
			if (it) activity!!.openSoftKeyBoard(binding.searchEdit)
			else activity!!.hideSoftKeyBoard()
		}
		
		//加载状态监听
		viewModel.isLoading.observeNonNull {
			binding.swipe.isRefreshing = it
			binding.swipe.isEnabled = it
			
		}
		
		//列表数据监听
		viewModel.searchList.observeNonNull {
			(binding.recSearchList.adapter as SearchListAdapter).replaceData(it)
		}
		
		//返回按钮
		binding.btnSend.setOnClickListener {
			activity?.finish()
		}
		
		//搜索输入框的监听
		binding.searchEdit.setOnEditorActionListener { view, actionId, keyEvent ->
			when (actionId) {
				EditorInfo.IME_ACTION_SEARCH -> loading()
			}
			false
		}
		
		//结果列表
		binding.recSearchList.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = SearchListAdapter(
				viewModel.searchList.value?: mutableListOf()
			) { position ->
			
			}
		}
	}
	
	override fun initData() {
	
	}
	
	private fun loading() {
		if (context!!.checkNet()) {
			viewModel.analysisAndSearch()
		} else {
			viewModel.isLoading.postValue(false)
		}
	}
	
	
}

