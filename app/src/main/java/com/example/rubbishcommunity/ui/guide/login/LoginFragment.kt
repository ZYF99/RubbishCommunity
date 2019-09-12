package com.example.rubbishcommunity.ui.guide.login

import android.Manifest
import android.animation.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.animation.addListener
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.LoginFragBinding
import com.example.rubbishcommunity.ui.MainActivity
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.ui.guide.AnimatorUtils
import com.example.rubbishcommunity.ui.guide.GuideCallBackCode
import com.example.rubbishcommunity.ui.guide.IGuide
import com.example.rubbishcommunity.utils.AppUtils
import com.example.rubbishcommunity.utils.PhoneUtils
import com.jakewharton.rxbinding2.view.RxView
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class LoginFragment : BindingFragment<LoginFragBinding, LoginViewModel>(
	LoginViewModel::class.java, R.layout.frag_login
) {

	
	private lateinit var animationUtils:AnimatorUtils
	
	override
	fun initBefore() {
	}
	
	@RequiresApi(Build.VERSION_CODES.O)
	@SuppressLint("CheckResult")
	override fun initWidget(view: View) {
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
		
		//登录按钮
		RxView.clicks(binding.btnLogin)
			.throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				//IMEI权限检查
				var phoneIMei: String
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (activity?.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
						//无权限时申请权限
						RxPermissions(activity as Activity).request(Manifest.permission.READ_PHONE_STATE)
							.subscribe {
								if (it) {
									//申请权限通过直接登陆
									phoneIMei = PhoneUtils.getPhoneIMEI(activity as Activity)
									login(phoneIMei)
								} else {
									activity?.finish()
								}
							}
					} else {
						//有权限，直接登录
						phoneIMei = PhoneUtils.getPhoneIMEI(activity as Activity)
						login(phoneIMei)
					}
				}
			}.bindLife()
		
		//注册按钮
		RxView.clicks(binding.btnRegister).throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				(activity as IGuide).jumpToRegister()
			}.bindLife()
	}
	
	//登陆
	private fun login(phoneIMei: String) {
		val userName = viewModel.userName.value!!
		val password = viewModel.password.value!!
		val versionName = AppUtils.getVersionName(context)
		val deviceBrand = PhoneUtils.deviceBrand
		val osVersion = PhoneUtils.systemVersion
		val systemModel = PhoneUtils.systemModel
		
		
		//开始登陆的动画
/*		binding.linAccount.visibility = View.INVISIBLE
		binding.linPassword.visibility = View.INVISIBLE*/
		animationUtils.startTransAnimation()
		
		//真实login
		viewModel.login(
			LoginOrRegisterRequestModel(
				LoginOrRegisterRequestModel.DeviceInfo(
					versionName,
					deviceBrand,
					phoneIMei,
					osVersion,
					systemModel
				), 0,
				password,
				//binding.editPassword.text.toString()
				false,
				userName
				//binding.editPassword.edit_email.toString()
			)
		).doOnSuccess { result ->
			when (result.meta.code) {
				//登录成功
				GuideCallBackCode.success -> {
					//持久化得到的token以及用户登录的信息
					viewModel.saveVerifyInfo(result.data.token)
					startActivity(Intent(context, MainActivity::class.java))
					(context as Activity).finish()
				}
				//登录失败
				else -> {
					MyApplication.showToast(result.meta.msg)
					animationUtils.complete()
				}
			}
		}.doOnError {
			MyApplication.showToast(it.toString())
			animationUtils.complete()
		}.bindLife()
		
	}
	
	override fun initData() {
	
	}
	
	

	
}