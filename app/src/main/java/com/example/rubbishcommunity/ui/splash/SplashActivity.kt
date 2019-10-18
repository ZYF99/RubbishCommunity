package com.example.rubbishcommunity.ui.splash

import android.content.Intent
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SplashBinding
import com.example.rubbishcommunity.ui.BindingActivity
import com.example.rubbishcommunity.ui.container.ContainerActivity
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class SplashActivity : BindingActivity<SplashBinding, SplashViewModel>() {
	
	override val clazz: Class<SplashViewModel> = SplashViewModel::class.java
	override val layRes: Int = R.layout.activity_splash
	
	override fun initBefore() {
	
	}
	
	override fun initWidget() {
		Single.timer(1, TimeUnit.SECONDS).doOnSuccess {
			startActivity(Intent(this, ContainerActivity::class.java))
			finish()
		}.bindLife()
	}
	
	override fun initData() {
	
	}
	
	
}
