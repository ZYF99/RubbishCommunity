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
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.RegisterFragBinding
import com.example.rubbishcommunity.persistence.getLoginState
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.guide.AnimatorUtils
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.ui.home.MainActivity
import com.jakewharton.rxbinding2.view.RxView
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.concurrent.TimeUnit


class RegisterFragment : BindingFragment<RegisterFragBinding, RegisterViewModel>(
	RegisterViewModel::class.java, R.layout.fragment_register
) {
	
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	
	}
	
	override fun onSoftKeyboardClosed() {
	
	}
	
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
			binding.btnNext
		)
	}
	
	@RequiresApi(Build.VERSION_CODES.O)
	@SuppressLint("CheckResult")
	override fun initWidget() {
		
		if (getLoginState()) {
			//已登陆
			//注册并登陆成功,跳转至完善信息界面主界面，由主界面判断是否需要完善信息
			startActivity(Intent(context, MainActivity::class.java))
			(context as Activity).finish()
		} else {
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
			//未登陆
			
			//下一步按钮
			RxView.clicks(binding.btnNext)
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
					(activity as ContainerActivity).replaceLogin()
				}.bindLife()
			
		}
		
		
	}
	
	//注册并登陆
	private fun registerAndLogin() {
		if (context!!.checkNet()) {
			//真实register
			viewModel.registerAndLogin()?.doOnSuccess {
				
				//注册并登陆成功,跳转至完善信息界面主界面，由主界面判断是否需要完善信息
				//完善信息成功，跳转至主界面
				startActivity(Intent(context, MainActivity::class.java))
				(context as Activity).finish()
				
				/*jumpToBasicInfo(context!!)*/
			}?.bindLife()
		}else{
			viewModel.isLoading.postValue(false)
		}
	}
	
	
	override fun initData() {
	
	}
	
	
	override fun onBackPressed(): Boolean {
		if (!isHidden) {
			(activity as ContainerActivity).replaceLogin()
			return true
		}
		
		return false
	}
	
	
}