package com.example.rubbishcommunity.ui.guide.welcome

import android.app.Activity
import android.view.inputmethod.EditorInfo
import android.widget.TextView.*
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.WelcomeBinding
import com.example.rubbishcommunity.persistence.getLoginState
import com.example.rubbishcommunity.ui.adapter.GalleryPagerTransformer
import com.example.rubbishcommunity.ui.adapter.PhotographyPagerAdapter
import com.example.rubbishcommunity.ui.container.jumpToRegister
import com.example.rubbishcommunity.ui.hideInput
import com.hankcs.hanlp.HanLP
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
		if(getLoginState()){
			jumpToRegister(context!!)
			activity?.finish()
		}
		
		binding.vm = viewModel
		binding.photographyPager.run{
			offscreenPageLimit = 2
			setPageTransformer(false,GalleryPagerTransformer())
		}
		
		
		binding.searchEdit.setOnEditorActionListener(OnEditorActionListener { p0, p1, p2 ->
			when (p1) {
				//点击Search
				EditorInfo.IME_ACTION_SEARCH -> {
					analysisAndSearch(viewModel.searchWord.value?:"")
					true
				}
				else -> false
			}
		})
		
		RxView.clicks(binding.btnRegister).throttleFirst(2,TimeUnit.SECONDS).doOnNext {
			jumpToRegister(context!!)
			activity?.finish()
		}.bindLife()
		
		viewModel.getPhotographyList().bindLife()
		viewModel.photograpgyList.observeNonNull {
			binding.photographyPager.adapter = PhotographyPagerAdapter(context!!,viewModel.photograpgyList.value?: listOf())
		}
		
		
		
	}
	
	override fun initData() {
	
	}
	
	private fun analysisAndSearch(str: String) {
		Single.just(str).flatMap {
			Single.just(HanLP.extractKeyword(it, 10))
		}.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.flatMap {
				MyApplication.showSuccess("解析结果：$it")
				viewModel.search(it[0])
			}.doOnSuccess {
				hideInput(activity as Activity)
			}.bindLife()
	}

	
}