package com.example.rubbishcommunity.ui.guide


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.fenrir_stage4.mainac.ui.widget.ContractDialog
import com.example.rubbishcommunity.ui.base.BindingActivity
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.GuideBinding
import com.example.rubbishcommunity.persistence.getLoginState
import com.example.rubbishcommunity.ui.MainActivity
import com.example.rubbishcommunity.ui.guide.login.LoginFragment
import com.example.rubbishcommunity.ui.guide.register.RegisterFragment
import com.example.rubbishcommunity.ui.widget.BottomDialogView
import com.example.rubbishcommunity.ui.widget.statushelper.StatusBarUtil
import com.example.rubbishcommunity.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit


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
	

	
	
	private var currentFragment: Fragment? = LoginFragment()
	
	override fun initBefore() {
	
	}
	
	@SuppressLint("ResourceType")
	override fun initWidget() {
		binding.vm = viewModel
		if (getLoginState()) {
			startActivity(Intent(this, MainActivity::class.java))
			finish()
		} else {
			//状态栏字体黑色
			StatusBarUtil.setStatusTextColor(true, this)
			supportFragmentManager.beginTransaction().apply {
				add(R.id.guideContainer, currentFragment as Fragment)
				commit()
			}
			//resolve error
			handleError()
		}
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
	
	


	
	
	fun showContractDialog() {
		hideKeyboard()
		
		val pop = ContractDialog(this)
		pop.show()
		//pop click listener
		pop.setOnClickListener(object : BottomDialogView.OnMyClickListener {
			override fun onFinishClick() {
				pop.dismiss()
			}
			
		})
		
		
	}
	
	
}
