package com.example.rubbishcommunity.ui.container


import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.Fragment
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BindingActivity
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ContainerBinding
import com.example.rubbishcommunity.initLocationOption
import com.example.rubbishcommunity.persistence.getLoginState
import com.example.rubbishcommunity.ui.guide.login.LoginFragment
import com.example.rubbishcommunity.ui.home.MainActivity
import com.example.rubbishcommunity.ui.guide.register.RegisterFragment
import com.example.rubbishcommunity.ui.release.dynamic.ReleaseDynamicFragment


class ContainerActivity : BindingActivity<ContainerBinding, ContainerViewModel>() {
	
	//跳转至register
	fun jumpToRegister() {
		replaceFragment("register")
	}
	
	//跳转至login
	fun jumpToLogin() {
		replaceFragment("login")
	}
	
	override val clazz: Class<ContainerViewModel> = ContainerViewModel::class.java
	override val layRes: Int = R.layout.activity_container
	private var currentFragment: Fragment? = Fragment()
	
	override fun initBefore() {
		if (intent.getStringExtra("tag") == null) {
			replaceFragment("login")
		} else {
			replaceFragment(intent.getStringExtra("tag")!!)
		}
	}
	
	@SuppressLint("ResourceType")
	override fun initWidget() {
		binding.vm = viewModel
		handleError()
		
	}
	
	override fun initData() {

	}
	
	private fun replaceFragment(tag: String) {
		if (currentFragment != null) {
			supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
		}
		currentFragment = supportFragmentManager.findFragmentByTag(tag)
		
		if (currentFragment == null) {
			currentFragment = when (tag) {
				"login" ->
					LoginFragment()
				
				"register" ->
					RegisterFragment()
				
				"releaseDynamic" ->
					ReleaseDynamicFragment()
				
				else -> Fragment()
			}
			supportFragmentManager.beginTransaction()
				.add(R.id.guideContainer, currentFragment!!, tag).commit()
		} else {
			supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
		}
	}
	
	
}
