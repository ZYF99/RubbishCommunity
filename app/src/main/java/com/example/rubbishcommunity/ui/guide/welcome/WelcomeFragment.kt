package com.example.rubbishcommunity.ui.guide.welcome

import android.view.inputmethod.EditorInfo
import android.widget.TextView.*
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.WelcomeBinding
import com.hankcs.hanlp.HanLP
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


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
		binding.vm = viewModel
		binding.searchEdit.setOnEditorActionListener(OnEditorActionListener { p0, p1, p2 ->
			when (p1) {
				//点击Search
				EditorInfo.IME_ACTION_SEARCH -> {
					analysisAndSearch(viewModel.searchWord.value!!)
					true
				}
				else -> false
			}
		})
		
	}
	
	override fun initData() {
	
	}
	
	private fun analysisAndSearch(str: String) {
		Single.just(str).flatMap {
			Single.just(HanLP.extractSummary(it, 10))
		}.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.flatMap {
				MyApplication.showSuccess("解析结果：$it")
				viewModel.search(it[0])
			}.doOnSuccess {
				hideKeyboard()
			}.bindLife()
	}
	
}