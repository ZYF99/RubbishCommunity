package com.example.rubbishcommunity.ui.home.find.dynamic

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicBinding
import com.example.rubbishcommunity.ui.home.MainActivity


class DynamicFragment : BindingFragment<DynamicBinding, DynamicViewModel>(
	DynamicViewModel::class.java, R.layout.frag_dynamic
) {
	override fun initBefore() {
	
	
	}
	
	@SuppressLint("CheckResult")
	override fun initWidget() {
		binding.vm = viewModel
		
		//viewModel.refreshing.observe { binding.refreshlayout.isRefreshing = it!! }
		
		viewModel.init()
		
		
		
		binding.recDynamic.run {
			layoutManager = LinearLayoutManager(context)
			adapter = DynamicListAdapter(viewModel.dynamicList.value)
		}
		
		
		binding.refreshLayout.setOnRefreshListener {
			when {
				!isNetworkAvailable() -> {
					(activity as MainActivity).showNetErrorSnackBar()
					viewModel.refreshing.postValue(false)
				}
				else -> {
					viewModel.getDynamicList()
				}
			}
		}
		
		
		viewModel.dynamicList.observe {
			binding.recDynamic.run {
				(adapter as DynamicListAdapter).replaceData(it!!)
			}
		}
		
		viewModel.refreshing.observe { isRefreshing ->
			binding.refreshLayout.run {
				if (!isRefreshing!!) finishRefresh()
			}
		}
		
		
	}
	
	override fun initData() {
	
	}
	
	
}