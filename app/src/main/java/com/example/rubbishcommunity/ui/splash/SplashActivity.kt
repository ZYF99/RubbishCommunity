package com.example.rubbishcommunity.ui.splash

import android.content.Intent
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SplashBinding
import com.example.rubbishcommunity.persistence.getClassificationList
import com.example.rubbishcommunity.ui.base.BindingActivity
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.utils.switchThread
import com.hankcs.hanlp.HanLP
import io.reactivex.Single
import java.util.concurrent.TimeUnit


class SplashActivity : BindingActivity<SplashBinding, SplashViewModel>() {
	
	override val clazz: Class<SplashViewModel> = SplashViewModel::class.java
	override val layRes: Int = R.layout.activity_splash
	
	override fun initBefore() {
		binding.vm = viewModel
	}
	
	
	override fun initWidget() {
		Single.fromCallable {
			HanLP.extractSummary("1", 1)
		}.switchThread()
			.bindLife()
	}
	
	override fun initData() {
		fun jumpToWelcome() = run {
			startActivity(Intent(this, ContainerActivity::class.java))
			finish()
		}
		
		if (getClassificationList().isNullOrEmpty())
			viewModel.fetchClassificationInfo { jumpToWelcome() }
		else Single.timer(1, TimeUnit.SECONDS)
			.doOnSuccess { jumpToWelcome() }.bindLife()
		
		
	}
	
	
}
