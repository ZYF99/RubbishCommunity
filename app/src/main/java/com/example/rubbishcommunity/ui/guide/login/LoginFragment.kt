package com.example.rubbishcommunity.ui.guide.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.LoginFragBinding
import com.example.rubbishcommunity.ui.MainActivity
import com.example.rubbishcommunity.persistence.saveLoginState
import com.example.rubbishcommunity.persistence.saveVerifyInfo
import com.example.rubbishcommunity.ui.guide.AnimatorUtils
import com.example.rubbishcommunity.ui.guide.GuideCallBackCode
import com.example.rubbishcommunity.ui.guide.IGuide
import com.example.rubbishcommunity.ui.guide.LGuideActivity
import com.jakewharton.rxbinding2.view.RxView
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.concurrent.TimeUnit


class LoginFragment : BindingFragment<LoginFragBinding, LoginViewModel>(
	LoginViewModel::class.java, R.layout.frag_login
) {
	
	private lateinit var animationUtils: AnimatorUtils
	
	override
	fun initBefore() {
		binding.vm = viewModel
		viewModel.init()
		//初始化动画工具
		animationUtils = AnimatorUtils(
			(binding.linContent.layoutParams as ViewGroup.MarginLayoutParams).leftMargin,
			(binding.linContent.layoutParams as ViewGroup.MarginLayoutParams).rightMargin,
			binding.progress,
			binding.linContent,
			binding.btnLogin
		)
	}
	
	
	@RequiresApi(Build.VERSION_CODES.O)
	override fun initWidget() {
		
		//观测是否在Loading
		viewModel.isLoading.observeNonNull {
			if (it) {
				//开始登陆的动画
				animationUtils.startTransAnimation()
			} else {
				//结束登陆的动画
				animationUtils.complete()
			}
		}
		
		
		//登录按钮
		RxView.clicks(binding.btnLogin)
			.throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				//IMEI权限检查
				if (activity?.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
					//无权限时申请权限
					RxPermissions(activity as Activity).request(Manifest.permission.READ_PHONE_STATE)
						.subscribe {
							if (it) {
								//申请权限通过直接登陆
								login()
							} else {
								activity?.finish()
							}
						}
				} else {
					//有权限，直接登录
					login()
				}
			}.bindLife()
		
		//注册按钮
		RxView.clicks(binding.btnRegister).throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				(activity as IGuide).jumpToRegister()
			}.bindLife()
		
		
		//服务协议按钮
		RxView.clicks(binding.btnContract).throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				//弹出服务协议窗口
				(activity as LGuideActivity).showContractDialog()
			}.bindLife()
		
		
	}
	
	//登陆
	@RequiresApi(Build.VERSION_CODES.O)
	private fun login() {
		//网络检查
		if (!isNetworkAvailable()) {
			(activity as LGuideActivity).showNetErrorSnackBar()
			return
		}
		//真实login
		viewModel.login()?.doOnSuccess { result ->
			when (result.meta.code) {
				//登录成功
				GuideCallBackCode.success -> {
					//持久化得到的token以及用户登录的信息
					saveVerifyInfo(
						viewModel.userName.value!!,
						viewModel.password.value!!,
						result.data.token
					)
					saveLoginState(true)
					startActivity(Intent(context, MainActivity::class.java))
					(context as Activity).finish()
				}
				//登录失败
				else -> {
					(activity as LGuideActivity).showErrorSnackBar(result.meta.msg)
				}
			}
		}?.bindLife()
	}
	
	
	override fun initData() {
	
	}
	
	
}