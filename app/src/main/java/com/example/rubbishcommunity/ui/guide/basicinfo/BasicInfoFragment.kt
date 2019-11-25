package com.example.rubbishcommunity.ui.guide.basicinfo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.baidu.location.LocationClient
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.BasicInfoFragBinding
import com.example.rubbishcommunity.ui.home.MainActivity
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.container.jumpToLogin
import com.example.rubbishcommunity.ui.guide.AnimatorUtils
import com.example.rubbishcommunity.ui.widget.DatePopView
import com.example.rubbishcommunity.utils.checkLocationPermissionAndGetLocation
import com.example.rubbishcommunity.ui.utils.showAvatarAlbum
import com.example.rubbishcommunity.utils.stringToDate
import com.jakewharton.rxbinding2.view.RxView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class BasicInfoFragment : BindingFragment<BasicInfoFragBinding, BasicInfoViewModel>(
	BasicInfoViewModel::class.java, R.layout.fragment_basic_info
) {
	
	private val locationClient by instance<LocationClient>()
	
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
	
	@RequiresApi(Build.VERSION_CODES.O)
	@SuppressLint("CheckResult", "SetTextI18n")
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
		
		
		//头像图片
		RxView.clicks(binding.imgAvatar).throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				context!!.showAvatarAlbum(this)
			}.bindLife()
		
		//性别图片
		RxView.clicks(binding.imgGender).throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				if (viewModel.gender.value == "男") {
					viewModel.gender.value = "女"
				} else {
					viewModel.gender.value = "男"
				}
			}.bindLife()
		
		//生日文本
		RxView.clicks(binding.tvBirthday).throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				DatePopView(context!!, object : DatePopView.OnMyClickListener {
					override fun onFinishClick(year: String, month: String, day: String) {
						viewModel.birthInt.value = stringToDate("$year$month$day").time
						viewModel.birthString.value = "$year$month$day"
					}
				}).show()
			}.bindLife()
		
		//发送验证码按钮
		RxView.clicks(binding.btnCode).throttleFirst(2, TimeUnit.SECONDS).doOnNext {
			//点击发送验证码
			sendEmail()
		}.bindLife()
		
		//进入社区按钮
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
								completeInfo()
							} else {
								MyApplication.showToast("您必须给予权限才能完成注册！")
							}
						}.bindLife()
				} else {
					//有权限，直接注册
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
		checkLocationPermissionAndGetLocation(
			activity!!, locationClient
		) {
			viewModel.location.postValue(it)
		}?.bindLife()
		
		
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
		Observable.interval(0, 1, TimeUnit.SECONDS).take(60).subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
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
		if (context!!.checkNet()) {
			viewModel.completeInfo()?.doOnSuccess {
				//完善信息成功并已将更新后的数据存入本地
				//跳转至主界面
				startActivity(Intent(context, MainActivity::class.java))
				(context as Activity).finish()
			}?.bindLife()
		}
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