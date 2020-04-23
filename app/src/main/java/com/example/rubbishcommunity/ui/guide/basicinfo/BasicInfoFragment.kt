package com.example.rubbishcommunity.ui.guide.basicinfo

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.BasicInfoFragBinding
import com.example.rubbishcommunity.ui.home.MainActivity
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.container.jumpToLogin
import com.example.rubbishcommunity.ui.guide.AnimatorUtils
import com.example.rubbishcommunity.ui.widget.DatePopView
import com.example.rubbishcommunity.ui.utils.showAvatarAlbum
import com.example.rubbishcommunity.utils.*
import com.jakewharton.rxbinding2.view.RxView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


class BasicInfoFragment : BindingFragment<BasicInfoFragBinding, BasicInfoViewModel>(
	BasicInfoViewModel::class.java, R.layout.fragment_basic_info
) {
	
	
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	
	}
	
	override fun onSoftKeyboardClosed() {
	
	}
	
	private lateinit var animationUtils: AnimatorUtils
	
	override fun initBefore() {
		binding.vm = viewModel
		
		//初始化动画工具
		animationUtils = AnimatorUtils(
			(binding.linContent.layoutParams as ViewGroup.MarginLayoutParams).leftMargin,
			(binding.linContent.layoutParams as ViewGroup.MarginLayoutParams).rightMargin,
			binding.progress,
			binding.linContent,
			binding.btnRegister
		)
		
	}

	override fun initWidget() {
		
		//观测是否在Loading
		viewModel.isRegisting.observeNonNull {
			if (it) {
				//开始登陆的动画
				animationUtils.startTransAnimation()
			} else {
				//结束登陆的动画
				animationUtils.complete()
			}
		}
		
		
		//头像图片
		binding.imgAvatar.setOnClickListener {
			showAvatarAlbum()
		}
		
		//性别图片
		binding.imgGender.setOnClickListener {
			if (viewModel.gender.value == "男") {
				viewModel.gender.value = "女"
			} else {
				viewModel.gender.value = "男"
			}
		}
		
		//生日文本
		binding.tvBirthday.setOnClickListener {
			DatePopView(context!!) { year, month, day ,birthLong ->
				viewModel.birthday.value = birthLong
			}.show()
		}
		
		
		//发送验证码按钮
		RxView.clicks(binding.btnCode).throttleFirst(2, TimeUnit.SECONDS).doOnNext {
			//点击发送验证码
			sendEmail()
		}.bindLife()
		
		//进入社区按钮
		RxView.clicks(binding.btnRegister)
			.throttleFirst(2, TimeUnit.SECONDS)
			.flatMap {
				//IMEI权限检查
				checkIMEIPermission().doOnNext {
					//申请权限通过直接登陆
					completeInfo()
				}
			}.bindLife()
		
		
		//去登陆按钮
		RxView.clicks(binding.tvLogin)
			.throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				//跳转至登陆界面
				jumpToLogin(context!!)
				(context as Activity).finish()
			}.bindLife()
		
		//获取定位
		
		
	}
	
	//初始化数据
	override fun initData() {
		//提示
		MyApplication.showWarning("请完善您的个人信息")
		//进入界面先发送验证码
		sendEmail()
	}
	
	//发送验证邮件
	private fun sendEmail() {
		viewModel.sendEmail().doOnSuccess {
			//成功发送验证码后倒计时
			countDown()
		}.bindLife()
	}
	
	//倒计时
	private fun countDown() {
		binding.btnCode.isEnabled = false
		Observable.interval(0, 1, TimeUnit.SECONDS)
			.take(60)
			.switchThread()
			.doOnNext {
				binding.btnCode.text = "${60 - it}秒"
			}.doOnComplete {
				binding.btnCode.text = "重新获取"
				binding.btnCode.isEnabled = true
			}.bindLife()
	}
	
	//完善信息并进入社区
	private fun completeInfo() {
		//完善信息
		context!!.checkNet().doOnComplete {
			viewModel.completeInfo()?.doOnSuccess {
				//完善信息成功并已将更新后的数据存入本地
				//跳转至主界面
				startActivity(Intent(context, MainActivity::class.java))
				(context as Activity).finish()
			}?.bindLife()
		}.doOnError {
			viewModel.isRegisting.postValue(false)
		}.bindLife()
	}
	
	//选图后的回调
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		val images: List<LocalMedia>
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
				images = PictureSelector.obtainMultipleResult(data)
				viewModel.upLoadAvatar(images[0].cutPath)
			}
		}
	}
	
	//back键
	override fun onBackPressed(): Boolean {
		if (!isHidden) {
			activity?.finish()
			return true
		}
		return false
	}
	
	
}