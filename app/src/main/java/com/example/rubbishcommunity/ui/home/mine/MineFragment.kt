package com.example.rubbishcommunity.ui.home.mine

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MineBinding
import com.example.rubbishcommunity.persistence.saveLoginState
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.ui.home.MainActivity
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView

class MineFragment : BindingFragment<MineBinding, MineViewModel>(
	MineViewModel::class.java, R.layout.fragment_mine
) {
	
	override fun initBefore() {
	
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		binding.collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
		
		//刷新状态监听
		viewModel.isRefreshing.observeNonNull {
			binding.refreshLayout.isRefreshing = it
			binding.rootLayout.isEnabled = !it
		}
		
		//注销按钮
		RxView.clicks(binding.btnLogout)
			.doOnNext {
				
				/*				//注销流
								viewModel.getUserInfo().doOnSuccess {
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
				startActivity(Intent(context, ContainerActivity::class.java))
				(context as Activity).finish()
				
				
			}.bindLife()
		
		//下拉刷新
		RxSwipeRefreshLayout.refreshes(binding.refreshLayout)
			.doOnNext {
				refresh()
			}
			.bindLife()
		
	}
	
	
	private fun refresh() {
		when {
			!isNetworkAvailable() -> {
				(activity as MainActivity).showNetErrorSnackBar()
				viewModel.isRefreshing.postValue(false)
			}
			else -> {
				viewModel.getUserInfo().bindLife()
			}
		}
	}
	
	override fun initData() {
		refresh()
	}


/*    //create pop of jobPicker
    private fun createJobPop() {
        hideKeyboard()
        binding.btnjobdown.setImageResource(R.drawable.icn_chevron_down_black)
        val pop = context?.let { ContractDialog(it) }
        pop?.show()
        //pop click listener
        pop?.setOnClickListener(object : BottomDialogView.OnMyClickListener {
            @SuppressLint("SetTextI18n")
            override fun onFinishClick() {
                binding.tvWork.text = pop.job
            }
        })

    }*/
	
	
}