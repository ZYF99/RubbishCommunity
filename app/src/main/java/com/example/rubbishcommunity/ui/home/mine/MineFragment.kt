package com.example.rubbishcommunity.ui.home.mine


import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MineBinding
import com.example.rubbishcommunity.persistence.saveLoginState
import com.example.rubbishcommunity.ui.container.jumoToPassword
import com.example.rubbishcommunity.ui.container.jumpToLogin
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
			binding.refreshLayout.isEnabled = it
			binding.rootLayout.isEnabled = !it
		}
		
		
		//账号安全按钮
		binding.btnSafe.setOnClickListener {
			jumoToPassword(context!!)
		}
		
		//注销按钮
		binding.btnLogout.setOnClickListener {
			//注销
			logout()
			
			
		}
		
		
		//下拉刷新
		RxSwipeRefreshLayout.refreshes(binding.refreshLayout)
			.doOnNext {
				refresh()
			}
			.bindLife()
		
	}
	
	
	private fun refresh() {
		context!!.checkNet().doOnComplete {
			viewModel.refreshUserInfo()
		}.doOnError {
			showNetErrorSnackBar()
			viewModel.isRefreshing.value = false
		}.bindLife()
	}
	
	private fun logout() {
		viewModel.logout()
			.doOnSuccess {
				//注销成功
				saveLoginState(false)
				activity?.finish()
				jumpToLogin(context!!)
			}.bindLife()
	}
	
	override fun initData() {
		refresh()
	}
	
	
}