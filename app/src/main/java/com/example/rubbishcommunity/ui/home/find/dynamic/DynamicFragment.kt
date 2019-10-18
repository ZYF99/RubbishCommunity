package com.example.rubbishcommunity.ui.home.find.dynamic

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicBinding
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
		
		viewModel.getDynamicList().doOnSubscribe {
			binding.recDynamic.run {
				(adapter as DynamicListAdapter).replaceData(viewModel.dynamicList.value!!)
			}
		}.bindLife()
		
		
		
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
					viewModel.getDynamicList().doOnSubscribe {
						binding.recDynamic.run {
							(adapter as DynamicListAdapter).replaceData(viewModel.dynamicList.value!!)
						}
					}.bindLife()
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