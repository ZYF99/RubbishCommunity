package com.example.rubbishcommunity.ui.guide


import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.fenrir_stage4.mainac.utils.ErrorType
import com.example.fenrir_stage4.mainac.utils.getErrorObs
import com.example.fenrir_stage4.mainac.utils.showNoWifiDialog
import com.example.fenrir_stage4.mainac.utils.showUnexpectedDialog
import com.example.rubbishcommunity.base.BindingActivity
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.GuideBinding
import com.example.rubbishcommunity.ui.guide.login.LoginFragment
import com.example.rubbishcommunity.ui.guide.register.RegisterFragment
import com.example.rubbishcommunity.ui.widget.statushelper.StatusBarUtil
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber


class LGuideActivity : BindingActivity<GuideBinding, GuideViewModel>(), IGuide {
	
	
	//跳转至register
	override fun jumpToRegister() {
		replaceFragment("registerOrLogin")
	}
	
	//跳转至login
	override fun jumpToLogin() {
		replaceFragment("login")

		
	}
	
	override val clazz: Class<GuideViewModel> = GuideViewModel::class.java
	override val layRes: Int = R.layout.activity_guide
	
	private var errorDisposable: Disposable? = null
	private var errorDialog: AlertDialog? = null
	
	
	private var currentFragment: Fragment? = LoginFragment()
	
	override fun initBefore() {
	
	}
	
	@SuppressLint("ResourceType")
	override fun initWidget() {
		//状态栏字体黑色
		StatusBarUtil.setStatusTextColor(true, this)
		
		supportFragmentManager.beginTransaction().apply {
			add(R.id.guideContainer, currentFragment as Fragment)
			commit()
		}
		//resolve error
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
			when (tag) {
				"login" ->
					currentFragment = LoginFragment()
				
				"registerOrLogin" ->
					currentFragment = RegisterFragment()
			}
			supportFragmentManager.beginTransaction()
				.add(R.id.guideContainer, currentFragment!!, tag).commit()
		} else {
			supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
		}
	}
	
	
	//实际'异常'处理者
	private fun handleError() {
		errorDisposable = getErrorObs()
			.observeOn(AndroidSchedulers.mainThread())
			.doOnNext {
				if (!errorDialog!!.isShowing) {
					errorDialog = when (it.errorType) {
						ErrorType.NO_WIFI -> showNoWifiDialog(this) {}
						else -> showUnexpectedDialog(this)
					}
				}
			}.subscribe({}, { Timber.e(it) })
	}
	
	override fun onDestroy() {
		errorDialog?.dismiss()
		errorDialog = null
		errorDisposable?.dispose()
		super.onDestroy()
	}
	
	fun showNetErrorSnackBar() {
		Snackbar.make(
			binding.root,
			R.string.net_unavailable,
			Snackbar.LENGTH_LONG
		).show()
	}
	
	
}
