package com.example.rubbishcommunity.ui.home.mine

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MineBinding
import com.example.rubbishcommunity.persistence.saveLoginState
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.ui.container.jumoToPassword
import com.example.rubbishcommunity.ui.home.MainActivity
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout

class MineFragment : BindingFragment<MineBinding, MineViewModel>(
	MineViewModel::class.java, R.layout.fragment_mine
) {
	
	override fun initBefore() {
	
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		//binding.collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
		
		//刷新状态监听
		viewModel.isRefreshing.observeNonNull {
			binding.refreshLayout.isRefreshing = it
			binding.rootLayout.isEnabled = !it
		}
		
		
		//账号安全按钮
		binding.btnSafe.setOnClickListener {
			jumoToPassword(context!!)
		}
		
		//注销按钮
		binding.btnLogout.setOnClickListener {
			/*				//注销流
				viewModel.getUserProfile().doOnSuccess {
					when (it.meta.code) {
						200 -> {
							//注销成功
							saveLoginState(false)
							startActivity(Intent(context, ContainerActivity::class.java))
							(context as Activity).finish()
						}
						else -> {
							//注销失败
							
						}
					}
				}*/
			
			//模拟注销成功
			//注销成功
			saveLoginState(false)
			(context as Activity).finish()
			val bundle = Bundle()
			bundle.putString("tag", "login")
			startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
		}
		
		
		//下拉刷新
		RxSwipeRefreshLayout.refreshes(binding.refreshLayout)
			.doOnNext {
				refresh()
			}
			.bindLife()
		
	}
	
	
	private fun refresh() {
		if (!isNetworkAvailable()) {
			(activity as MainActivity).showNetErrorSnackBar()
			viewModel.isRefreshing.value = false
		} else {
			viewModel.refreshUserInfo()
		}
	}
	
	override fun initData() {
		refresh()
	}
	
	
}