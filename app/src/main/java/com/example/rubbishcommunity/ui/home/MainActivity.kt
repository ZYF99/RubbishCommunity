package com.example.rubbishcommunity.ui.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MainBinding
import com.example.rubbishcommunity.persistence.getLocalNeedMoreInfo
import com.example.rubbishcommunity.persistence.getLocalVerifiedEmail
import com.example.rubbishcommunity.service.MqServiceConnection
import com.example.rubbishcommunity.ui.base.BindingActivity
import com.example.rubbishcommunity.ui.container.jumpToBasicInfo
import com.example.rubbishcommunity.ui.container.jumpToReleaseMoments
import com.example.rubbishcommunity.ui.home.find.FindFragment
import com.example.rubbishcommunity.ui.home.homepage.HomePageFragment
import com.example.rubbishcommunity.ui.home.message.MessageFragment
import com.example.rubbishcommunity.ui.home.mine.MineFragment
import com.example.rubbishcommunity.ui.release.moments.MomentsType
import com.example.rubbishcommunity.ui.utils.dp2px
import com.example.rubbishcommunity.ui.widget.statushelper.StatusBarUtil
import com.jakewharton.rxbinding2.view.RxView
import com.zzhoujay.richtext.RichText
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class MainActivity : BindingActivity<MainBinding, MainViewModel>() {
	//纪录第一次点击back时的时间戳（以毫秒为单位的时间）
	private var quiteTime: Long = System.currentTimeMillis()
	override val clazz: Class<MainViewModel> = MainViewModel::class.java
	override val layRes: Int = R.layout.activity_main
	private var currentFragment: Fragment? = HomePageFragment()
	private val mqServiceConnection = MqServiceConnection()
	
	override fun initBefore() {
	
	}
	
	@SuppressLint("ResourceType")
	override fun initWidget() {
		//状态栏字体黑色
		StatusBarUtil.setStatusTextColor(true, this)
/*		window.setFlags(
			FLAG_LAYOUT_NO_LIMITS,
			FLAG_LAYOUT_NO_LIMITS
		)*/
		
		viewModel.appBarOffsetBias.observeNonNull {
			binding.bottomnavigation.translationY =
				it * (binding.bottomnavigation.height + dp2px(16f))
			binding.fabAdd.alpha = ((1 - (it * 0.5)).toFloat())
/*				it.times(
					binding.bottomnavigation.height
				)*/
		}
		
		
		//不需要验证邮箱和完善信息,初始化home界面
		supportFragmentManager.beginTransaction().apply {
			add(R.id.maincontainer, currentFragment as Fragment)
			commit()
		}
		
		RichText.initCacheDir(this)
		
		//底部导航栏
		binding.bottomnavigation.setOnNavigationItemSelectedListener {
			when (it.itemId) {
				R.id.navigation_home -> {
					replaceFragment("home")
				}
				R.id.navigation_find -> {
					replaceFragment("find")
				}
				R.id.navigation_null -> {
				}
				R.id.navigation_message -> {
					replaceFragment("message")
				}
				R.id.navigation_mine -> {
					replaceFragment("mine")
				}
			}
			true
		}
		
		//添加fab
		RxView.clicks(binding.fabAdd).doOnNext {
			showAddDialog()
		}.bindLife()
		
		handleError()
		
		//判断是否需要完善信息
		if (!getLocalVerifiedEmail() && getLocalNeedMoreInfo()) {
			//需要验证邮箱和完善信息,跳转至完善信息界面
			Single.timer(200, TimeUnit.MILLISECONDS).doOnSuccess {
				jumpToBasicInfo(this)
				finish()
			}.bindLife()
		}
		
	}
	
	override fun initData() {

/*		//启动mqtt服务
		bindService(
			Intent(this, MyMqttService::class.java),
			mqServiceConnection,
			Context.BIND_AUTO_CREATE
		)*/
		
	}
	
	
	private fun publishMqMsg(string: String) {
/*		//发布消息
		mqServiceConnection.getMqttService().publishMessage(string)*/
		
	}
	
	private fun replaceFragment(tag: String) {
		if (currentFragment != null) {
			supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
		}
		currentFragment = supportFragmentManager.findFragmentByTag(tag)
		
		if (currentFragment == null) {
			when (tag) {
				"home" ->
					currentFragment = HomePageFragment()
				
				"find" ->
					currentFragment = FindFragment()
				
				"message" ->
					currentFragment = MessageFragment()
				
				"mine" ->
					currentFragment = MineFragment()
			}
			supportFragmentManager.beginTransaction()
				.add(R.id.maincontainer, currentFragment!!, tag).commit()
		} else {
			supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
		}
	}
	
	private fun showAddDialog() {
		AddDialog(
			this,
			object : AddDialog.OnAddDialogClickListener {
				override fun onDynamicClick() {
					//开启发布动态界面
					jumpToReleaseMoments(this@MainActivity, MomentsType.TYPE_DYNAMIC)
				}
				
				override fun onRecoveryClick() {
					//开启发布回收界面
					jumpToReleaseMoments(this@MainActivity, MomentsType.TYPE_RECOVERY)
				}
				
				override fun onDismiss() {
				
				}
				
			}).showAbove(binding.fabAdd)
	}
	
	override fun onDestroy() {
		try {
			unbindService(mqServiceConnection)
		} catch (e: Exception) {
			e.printStackTrace()
		}
		RichText.recycle()
		super.onDestroy()
	}

/*	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (keyCode == KEYCODE_BACK) {
			val intent = Intent(Intent.ACTION_MAIN)
			intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
			intent.addCategory(Intent.CATEGORY_HOME)
			startActivity(intent)
			return true
		}
		return super.onKeyDown(keyCode, event)
	}*/
	
	override fun onBackPressed() {
		if (System.currentTimeMillis() - quiteTime > 3000) {
			Toast.makeText(
				this, "再次点击返回键退出APP", Toast.LENGTH_SHORT
			).show()
			quiteTime = System.currentTimeMillis()
		} else {
			finish()
		}
	}
	
}
