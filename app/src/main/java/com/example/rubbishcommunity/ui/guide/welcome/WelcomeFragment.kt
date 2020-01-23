package com.example.rubbishcommunity.ui.guide.welcome

import android.app.Activity
import android.content.Intent
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.WelcomeBinding
import com.example.rubbishcommunity.persistence.getLoginState
import com.example.rubbishcommunity.ui.adapter.PagerScaleTransformer
import com.example.rubbishcommunity.ui.adapter.PhotographyPagerAdapter
import com.example.rubbishcommunity.ui.container.jumpToRegister
import com.example.rubbishcommunity.ui.home.MainActivity
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.concurrent.TimeUnit


class WelcomeFragment : BindingFragment<WelcomeBinding, WelcomeViewModel>(
	WelcomeViewModel::class.java, R.layout.fragment_welcome
) {
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	}
	
	override fun onSoftKeyboardClosed() {
	}
	
	override fun initBefore() {
		if (getLoginState()) {
			//已登陆
			//注册并登陆成功,跳转至完善信息界面主界面，由主界面判断是否需要完善信息
			startActivity(Intent(context, MainActivity::class.java))
			(context as Activity).finish()
		}
	}
	
	
	override fun initWidget() {
			binding.vm = viewModel
			binding.photographyPager.run {
				offscreenPageLimit = 2
				setPageTransformer(false, PagerScaleTransformer())
			}
			binding.searchEdit.run {
				RxTextView.textChanges(this)
					.debounce(1, TimeUnit.SECONDS)
					.skip(1)
					.doOnNext {
						viewModel.search()
					}.bindLife()
			}
			RxView.clicks(binding.btnRegister).throttleFirst(2, TimeUnit.SECONDS).doOnNext {
				jumpToRegister(context!!)
				activity?.finish()
			}.bindLife()
			
			viewModel.searchResultList.observeNonNull {
				binding.photographyPager.adapter =
					PhotographyPagerAdapter(context!!, viewModel.searchResultList.value ?: listOf())
				(binding.photographyPager.adapter as PhotographyPagerAdapter).notifyDataSetChanged()
			}
	}
	
	override fun initData() {
		viewModel.search()
	}
	
}