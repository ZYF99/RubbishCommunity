package com.example.rubbishcommunity.ui.home.find.dynamic

import android.annotation.SuppressLint
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicBinding
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.ui.container.jumpToDynamicDetail
import com.example.rubbishcommunity.ui.home.MainActivity


class DynamicFragment : BindingFragment<DynamicBinding, DynamicViewModel>(
	DynamicViewModel::class.java, R.layout.fragment_dynamic
) {
	override fun initBefore() {
	
	
	}
	
	@SuppressLint("CheckResult")
	override fun initWidget() {
		binding.vm = viewModel
		
		//viewModel.isRefreshing.observe { binding.refreshlayout.isRefreshing = it!! }
		
		viewModel.init()
		
		
		
		binding.recDynamic.run {
			layoutManager = LinearLayoutManager(context)
			adapter = DynamicListAdapter(viewModel.dynamicList.value)
			(adapter as DynamicListAdapter).setOnItemClickListener { adapter, view, position ->
				//开启详情页
				viewModel.dynamicList.value!![position].id?.let { jumpToDynamicDetail(context, it) }
			}
		}
		
		
		binding.refreshLayout.setOnRefreshListener {
			when {
				!isNetworkAvailable() -> {
					(activity as MainActivity).showNetErrorSnackBar()
					viewModel.isRefreshing.postValue(false)
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
		
		viewModel.isRefreshing.observe { isRefreshing ->
			binding.refreshLayout.run {
				if (!isRefreshing!!) finishRefresh()
			}
		}
		
		
	}
	
	override fun initData() {
	
	}
	
	
}