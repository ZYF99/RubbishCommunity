package com.example.rubbishcommunity.ui.home.homepage.search


import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SearchBinding
import com.example.rubbishcommunity.initLocationClient
import com.example.rubbishcommunity.ui.base.BindingActivity
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.ui.container.jumpToCameraSearch
import com.example.rubbishcommunity.ui.utils.hideSoftKeyBoard
import com.example.rubbishcommunity.ui.utils.openSoftKeyBoard
import com.example.rubbishcommunity.utils.checkLocationPermissionAndGetLocation
import com.example.rubbishcommunity.utils.showLocationServiceDialog
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.concurrent.TimeUnit


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
		
		
		
		
		(activity as ContainerActivity).viewModel.run {
			keyWordFromCamera.observeNonNull {
				viewModel.searchKey.postValue(it)
			}
		}
		
		
		//搜索输入框的监听
		RxTextView.textChanges(binding.searchEdit)
			.debounce(1, TimeUnit.SECONDS)
			.skip(1)
			.doOnNext {
				searchKey()
			}.bindLife()
		
		
		//结果列表
		binding.recSearchList.run {
			layoutManager = LinearLayoutManager(context)
			adapter = SearchListAdapter(
				viewModel.searchList.value ?: mutableListOf()
			) { position ->
			
			}
		}
		
		
		//拍照搜索按钮
		binding.btnCamera.setOnClickListener {
			jumpToCameraSearch(this)
		}
		
		//语音搜索按钮
		binding.btnMac.setOnClickListener {
		
		
		}
	}
	
	override fun initData() {
		getLocation()
	}
	
	//查询关键字分类
	private fun searchKey() {
		context!!.checkNet().doOnComplete {
			if (viewModel.searchKey.value!!.isNotEmpty()) {
				viewModel.analysisAndSearch()
			}
		}.doOnError {
			viewModel.isLoading.postValue(false)
		}.bindLife()
	}
	
	//获取定位
	private fun getLocation() {
		checkLocationPermissionAndGetLocation(
			initLocationClient(context!!)
		).doOnNext {
			viewModel.location.postValue(it)
		}.doOnError {
			//当没有定位权限时
			showLocationServiceDialog {
				getLocation()
			}
		}.bindLife()
	}
	
}

