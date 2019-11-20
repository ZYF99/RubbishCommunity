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
import com.example.rubbishcommunity.ui.home.MainActivity
import com.example.rubbishcommunity.ui.guide.AnimatorUtils
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.ui.container.jumoToPassword
import com.example.rubbishcommunity.ui.base.hideInput
import com.example.rubbishcommunity.ui.widget.ContractDialog
import com.tbruyelle.rxpermissions2.RxPermissions


class LoginFragment : BindingFragment<LoginFragBinding, LoginViewModel>(
	LoginViewModel::class.java, R.layout.fragment_login
) {
	
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	
	}
	
	override fun onSoftKeyboardClosed() {
	
	}
	
	//动画工具
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
		
		binding.btnLogin.setOnClickListener {
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
		}
		
		
		//注册按钮
		binding.btnRegister.setOnClickListener {
			(activity as ContainerActivity).replaceRegister()
		}
		//服务协议按钮
		binding.btnForgetPassword.setOnClickListener {
			//跳转至修改密码界面
			jumoToPassword(context!!)
		}
		//服务协议按钮
		binding.btnContract.setOnClickListener {
			//弹出服务协议窗口
			showContractDialog()
		}
		
	}
	
	//登陆
	@RequiresApi(Build.VERSION_CODES.O)
	private fun login() {
		//网络检查
		if (!isNetworkAvailable()) {
			(activity as ContainerActivity).showNetErrorSnackBar()
			return
		}
		//真实login
		viewModel.login()?.doOnSuccess {
			//登录成功
			startActivity(Intent(context, MainActivity::class.java))
			(context as Activity).finish()
		}?.bindLife()
	}
	
	
	override fun initData() {
	
	}
	
	
	//协议弹窗
	private fun showContractDialog() {
		hideInput(activity as Activity)
		ContractDialog(context) { //onFinishClick event
		
		}.show()
	}
	
	override fun onBackPressed(): Boolean {
		if (!isHidden) {
			activity?.finish()
			return true
		}
		return false
	}
	
}