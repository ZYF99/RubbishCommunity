package com.example.rubbishcommunity.ui.guide.password

import android.os.Build
import com.example.rubbishcommunity.databinding.PasswordFragBinding
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.ui.guide.AnimatorUtils
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit


const val STEP_INPUT_EMAIL = 1
const val STEP_INPUT_CODE = 2
const val STEP_INPUT_PASSWORD = 3

class PasswordFragment : BindingFragment<PasswordFragBinding, PasswordViewModel>(
	PasswordViewModel::class.java, R.layout.fragment_password
) {
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	
	}
	
	override fun onSoftKeyboardClosed() {
	
	}
	
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
			binding.btnNext
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
		
		
		//上一步按钮
		RxView.clicks(binding.btnBack)
			.throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				activity?.finish()
			}.bindLife()
		
		//下一步按钮
		RxView.clicks(binding.btnNext)
			.throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				when (viewModel.currentStep.value) {
					STEP_INPUT_EMAIL -> {
						sendEmail()
					}
					STEP_INPUT_CODE -> {
						sendVerifyCode()
					}
					STEP_INPUT_PASSWORD -> {
						sendPassword()
					}
					else -> {
					
					}
				}
				
			}.bindLife()
		
		
	}
	
	//发送邮箱
	private fun sendEmail() {
		//网络检查
		context!!.checkNet().doOnComplete {
			//发送邮箱验证码
			viewModel.sendEmail()?.doOnSuccess {
				//发送成功
			}?.bindLife()
		}.doOnError {
			viewModel.isLoading.postValue(false)
		}.bindLife()
		
	}
	
	//发送验证码
	private fun sendVerifyCode() {
		//网络检查
		context!!.checkNet().doOnComplete {
			//发送邮箱验证码
			viewModel.sendVerifyCode()?.doOnSuccess {
				//发送成功
			}?.bindLife()
		}.doOnError {
			viewModel.isLoading.postValue(false)
		}.bindLife()
	}
	
	//发送新密码至服务器
	private fun sendPassword() {
		//网络检查
		context!!.checkNet().doOnComplete {
			//发送新密码至服务器
			viewModel.editPassword()?.doOnSuccess {
				//修改密码成功
			}?.bindLife()
		}.doOnError {
			viewModel.isLoading.postValue(false)
		}.bindLife()
		
	}
	
	
	override fun initData() {
	
	}
	
	
	override fun onBackPressed(): Boolean {
		if (!isHidden) {
			activity?.finish()
			return true
		}
		return false
	}
	
	
}