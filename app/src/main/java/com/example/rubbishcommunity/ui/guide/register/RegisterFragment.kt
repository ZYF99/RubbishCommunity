package com.example.rubbishcommunity.ui.guide.register

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.RegisterFragBinding
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.ui.MainActivity
import com.example.rubbishcommunity.ui.guide.AnimatorUtils
import com.example.rubbishcommunity.ui.guide.GuideCallBackCode
import com.example.rubbishcommunity.ui.guide.IGuide
import com.example.rubbishcommunity.utils.AppUtils
import com.example.rubbishcommunity.utils.PhoneUtils
import com.jakewharton.rxbinding2.view.RxView
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.concurrent.TimeUnit


class RegisterFragment : BindingFragment<RegisterFragBinding, RegisterViewModel>(
	RegisterViewModel::class.java, R.layout.frag_register
) {
	
	private lateinit var animationUtils:AnimatorUtils
	
	override fun initBefore() {
	
	
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
			binding.btnRegister
		)
		
		//注册按钮
		RxView.clicks(binding.btnRegister)
			.throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				
				animationUtils.startTransAnimation()

				
				
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
									registerAndLogin(phoneIMei)
								} else {
									MyApplication.showToast("您必须给予权限才能完成注册！")
									animationUtils.complete()
								}
							}
					} else {
						//有权限，直接注册
						phoneIMei = PhoneUtils.getPhoneIMEI(activity as Activity)
						registerAndLogin(phoneIMei)
					}
				}
			}.bindLife()
	}
	
	//登陆
	private fun registerAndLogin(phoneIMei: String) {
		val userName = viewModel.userName.value!!
		val password = viewModel.password.value!!
		val rePassword = viewModel.rePassword.value!!
		if (password != rePassword) {
			MyApplication.showToast("两次密码不一致")
			return
		}
		val versionName = AppUtils.getVersionName(context)
		val deviceBrand = PhoneUtils.deviceBrand
		val osVersion = PhoneUtils.systemVersion
		val systemModel = PhoneUtils.systemModel

		//真实register
		viewModel.registerOrLogin(
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
				true,
				userName
				//binding.editPassword.edit_email.toString()
			)
		).doOnSuccess { result ->
			MyApplication.showToast(result.meta.msg)
			when (result.meta.code) {
				//注册成功
				GuideCallBackCode.success -> {
					//真实register
					viewModel.registerOrLogin(
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
					).doOnSuccess {
						MyApplication.showToast(result.meta.msg)
						when (result.meta.code) {
							//登陆成功
							GuideCallBackCode.success -> {
								//持久化得到的token以及用户登录的信息
								viewModel.saveVerifyInfo(result.data.token)
								//跳转到首页
								startActivity(Intent(context, MainActivity::class.java))
								(context as Activity).finish()
							}
							//登陆失败
							else -> {
								animationUtils.complete()
							}
						}
					}.doOnError {
						MyApplication.showToast(it.toString())
					}.bindLife()
				}
				//注册失败
				else -> {
					animationUtils.complete()
				}
			}
		}.doOnError {
			MyApplication.showToast(it.toString())
		}.bindLife()
		
	}
	
	
	override fun initData() {
	
	}
	
	override fun onBackPressed(): Boolean {
		(activity as IGuide).jumpToLogin()
		return true
	}
	
	
}