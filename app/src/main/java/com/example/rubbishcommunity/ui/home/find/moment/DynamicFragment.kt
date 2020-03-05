package com.example.rubbishcommunity.ui.home.find.moment

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DynamicBinding
import com.example.rubbishcommunity.model.api.mine.UsrProfile
import com.example.rubbishcommunity.ui.container.jumpToDynamicDetail
import com.example.rubbishcommunity.ui.widget.UserInfoDialog


class DynamicFragment : BindingFragment<DynamicBinding, DynamicViewModel>(
	DynamicViewModel::class.java, R.layout.fragment_dynamic
) {
	override fun initBefore() {
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		
		viewModel.isRefreshing.observeNonNull { isRefreshing ->
			binding.refreshLayout.run {
				if (!isRefreshing!!) finishRefresh()
			}
		}
		
		viewModel.dynamicList.observeNonNull {
			(binding.recDynamic.adapter as DynamicListAdapter).replaceData(it)
		}
		
		//下拉刷新控件
		binding.refreshLayout.setOnRefreshListener {
			refresh()
		}
		
		//动态列表
		binding.recDynamic.run {
			layoutManager = LinearLayoutManager(context)
			adapter = DynamicListAdapter(viewModel.dynamicList.value ?: mutableListOf(),
				{ position ->
					//开启详情页
					jumpToDynamicDetail(context, viewModel.dynamicList.value!![position].id!!)
				},
				{ position ->
					UserInfoDialog(
						context,
						UsrProfile.getDefault()
					) {
						//TODO 点击后跳转主页
					}.show()
				})
		}
	}
	
	override fun initData() {
		refresh()
	}
	
	private fun refresh() {
		context!!.checkNet().doOnComplete {
			viewModel.getDynamicList()
		}.doOnError {
			viewModel.isRefreshing.postValue(false)
		}.bindLife()
	}
	
}