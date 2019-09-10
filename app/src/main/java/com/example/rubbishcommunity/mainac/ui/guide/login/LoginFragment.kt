package com.example.rubbishcommunity.mainac.ui.guide.login

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.example.rubbishcommunity.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.LoginFragBinding
import android.graphics.drawable.Drawable
import com.example.rubbishcommunity.mainac.ui.MainActivity
import com.jakewharton.rxbinding2.view.RxView


class LoginFragment : BindingFragment<LoginFragBinding, LoginViewModel>(
	LoginViewModel::class.java, R.layout.frag_login
) {
	override fun initBefore() {
	
	
	}
	
	@SuppressLint("CheckResult")
	override fun initWidget(view: View) {
		
		binding.vm = viewModel
		viewModel.init()
		
		RxView.clicks(binding.btnLogin).doOnNext {
			
			startActivity(Intent(context, MainActivity::class.java))
			
		}.bindLife()

/*
        binding.gridHotword.run {
            val hotWordList = listOf("苹果","苹果","苹果","苹果","苹果","苹果","苹果","苹果")
            adapter = TagGridAdapter(getContext(),hotWordList)
        }
*/
	
	
	}
	
	override fun initData() {
	
	}
	
	
}