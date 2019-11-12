package com.example.rubbishcommunity.ui.guide.welcome

import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.WelcomeBinding
import com.example.rubbishcommunity.persistence.getLoginState
import com.example.rubbishcommunity.ui.adapter.GalleryPagerTransformer
import com.example.rubbishcommunity.ui.adapter.PhotographyPagerAdapter
import com.example.rubbishcommunity.ui.container.jumpToRegister
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
	}
	
	
	override fun initWidget() {
		if (getLoginState()) {
			jumpToRegister(context!!)
		}
		binding.vm = viewModel
		binding.photographyPager.run {
			offscreenPageLimit = 2
			setPageTransformer(false, GalleryPagerTransformer())
		}
		binding.searchEdit.run {
			RxTextView.textChanges(this)
				.debounce(1, TimeUnit.SECONDS).skip(1)
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
		viewModel.searchWord.value = "纸巾"
		viewModel.search()
	}

	
	
}