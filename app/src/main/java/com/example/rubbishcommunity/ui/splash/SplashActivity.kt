package com.example.rubbishcommunity.ui.splash

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.SplashBinding
import com.example.rubbishcommunity.ui.base.BindingActivity
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import com.example.rubbishcommunity.utils.switchThread
import com.hankcs.hanlp.HanLP
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SplashActivity : BindingActivity<SplashBinding, SplashViewModel>() {
	
	override val clazz: Class<SplashViewModel> = SplashViewModel::class.java
	override val layRes: Int = R.layout.activity_splash
	
	override fun initBefore() {
		binding.vm = viewModel
	}
	
	@RequiresApi(Build.VERSION_CODES.O)
	override fun initWidget() {
		Single.fromCallable {
			HanLP.extractSummary("1", 1)
		}.subscribeOn(Schedulers.io())
			.bindLife()
		
		viewModel.initClassification()
			.switchThread()
			.doOnSuccess {
				startActivity(Intent(this, ContainerActivity::class.java))
				finish()
			}
			.doOnError {
				sendError(ErrorType.API_ERROR, it.message ?: "")
			}.bindLife()
		
		
	}
	
	override fun initData() {
	
	}
	
	
}
