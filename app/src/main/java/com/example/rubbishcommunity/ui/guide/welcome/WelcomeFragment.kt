package com.example.rubbishcommunity.ui.guide.welcome

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
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
import com.jakewharton.rxbinding2.widget.RxTextView
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
		
		
		binding.searchEdit.run{
			RxTextView.textChanges(this)
				.debounce(1,TimeUnit.SECONDS).skip(1)
				.doOnNext {
					analysisAndSearch(viewModel.searchWord.value?:"")
				}.bindLife()
		}
		
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
			Single.just(HanLP.extractSummary(it, 1))
		}.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.flatMap {
				viewModel.search(it[0])
			}.doOnSuccess {
				MyApplication.showSuccess(it.data.toString())
			}.bindLife()
	}

	
}