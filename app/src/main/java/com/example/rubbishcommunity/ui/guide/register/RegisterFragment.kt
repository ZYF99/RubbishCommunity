package com.example.rubbishcommunity.ui.guide.register

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.RegisterFragBinding
import com.example.rubbishcommunity.persistence.saveLoginState
import com.example.rubbishcommunity.persistence.saveVerifyInfo
import com.example.rubbishcommunity.ui.MainActivity
import com.example.rubbishcommunity.ui.guide.AnimatorUtils
import com.example.rubbishcommunity.ui.guide.GuideCallBackCode
import com.example.rubbishcommunity.ui.guide.IGuide
import com.example.rubbishcommunity.ui.guide.LGuideActivity
import com.jakewharton.rxbinding2.view.RxView
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.concurrent.TimeUnit


class RegisterFragment : BindingFragment<RegisterFragBinding, RegisterViewModel>(
	RegisterViewModel::class.java, R.layout.frag_register
) {
	
	private lateinit var animationUtils: AnimatorUtils
	
	override fun initBefore() {
		binding.vm = viewModel
		viewModel.init()
		//初始化动画工具
		animationUtils = AnimatorUtils(
			(binding.linContent.layoutParams as ViewGroup.MarginLayoutParams).leftMargin,
			(binding.linContent.layoutParams as ViewGroup.MarginLayoutParams).rightMargin,
			binding.progress,
			binding.linContent,
			binding.btnRegister
		)
	}
	
	@RequiresApi(Build.VERSION_CODES.O)
	@SuppressLint("CheckResult")
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
		
		//观察错误提示信息
		viewModel.errorMsg.observeNonNull {
			(activity as LGuideActivity).showErrorSnackBar(it)
		}
		
		
		//注册按钮
		RxView.clicks(binding.btnRegister)
			.throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				//IMEI权限检查
				
				if (activity?.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
					//无权限时申请权限
					RxPermissions(activity as Activity).request(Manifest.permission.READ_PHONE_STATE)
						.subscribe {
							if (it) {
								//申请权限通过直接登陆
								registerAndLogin()
							} else {
								MyApplication.showToast("您必须给予权限才能完成注册！")
							}
						}
				} else {
					//有权限，直接注册
					registerAndLogin()
				}
				
			}.bindLife()
		
		
		//返回登陆按钮
		RxView.clicks(binding.btnBack).throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				(activity as IGuide).jumpToLogin()
			}.bindLife()
		
		//服务协议按钮
		RxView.clicks(binding.btnContract).throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				(activity as LGuideActivity).showContractDialog()
			}.bindLife()
		
	}
	
	//注册并登陆
	private fun registerAndLogin() {
		
		if (!isNetworkAvailable()) {
			(activity as LGuideActivity).showNetErrorSnackBar()
			return
		}
		
		//真实register
		viewModel.registerOrLogin()?.doOnSuccess {
			when (it.meta.code) {
				//登陆成功
				GuideCallBackCode.success -> {
					//持久化得到的token以及用户登录的信息
					saveVerifyInfo(
						viewModel.userName.value!!,
						viewModel.password.value!!,
						it.data.token
					)
					saveLoginState(true)
					//跳转到首页
					startActivity(Intent(context, MainActivity::class.java))
					(context as Activity).finish()
				}
				//登陆失败
				else -> {
					MyApplication.showToast(it.meta.msg)
				}
			}
		}?.bindLife()
		
		
	}
	
	
	override fun initData() {
	
	}
	
	
	override fun onBackPressed(): Boolean {
		(activity as IGuide).jumpToLogin()
		return true
	}
	
	
}