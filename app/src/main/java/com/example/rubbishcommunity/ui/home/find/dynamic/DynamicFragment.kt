package com.example.rubbishcommunity.ui.home.find.dynamic

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicBinding
import com.example.rubbishcommunity.ui.container.jumpToDynamicDetail
import com.example.rubbishcommunity.ui.home.MainActivity


class DynamicFragment : BindingFragment<DynamicBinding, DynamicViewModel>(
	DynamicViewModel::class.java, R.layout.fragment_dynamic
) {
	override fun initBefore() {
	
	
	}
	
	override fun initWidget() {
		
		binding.vm = viewModel
		viewModel.getDynamicList()
		
		viewModel.dynamicList.observeNonNull {
			(binding.recDynamic.adapter as DynamicListAdapter).replaceData(it)
		}
		
		binding.recDynamic.run {
			layoutManager = LinearLayoutManager(context)
			adapter = DynamicListAdapter(viewModel.dynamicList.value)
			(adapter as DynamicListAdapter).setOnItemClickListener { _, _, position ->
				//开启详情页
				jumpToDynamicDetail(context,viewModel.dynamicList.value!![position].id!!)
			}
		}
		
		
		binding.refreshLayout.setOnRefreshListener {
			when {
				!isNetworkAvailable() -> {
					(activity as MainActivity).showNetErrorSnackBar()
					viewModel.isRefreshing.value = false
				}
				else -> {
					viewModel.getDynamicList()
				}
			}
		}
		
		viewModel.isRefreshing.observe { isRefreshing ->
			binding.refreshLayout.run {
				if (!isRefreshing!!) finishRefresh()
			}
		}
		
		
	}
	
	override fun initData() {
	
	}
	
	
}