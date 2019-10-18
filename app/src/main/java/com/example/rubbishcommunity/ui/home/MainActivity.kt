package com.example.rubbishcommunity.ui.home


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.example.rubbishcommunity.ui.home.find.FindFragment
import com.example.rubbishcommunity.ui.BindingActivity
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MainBinding
import com.example.rubbishcommunity.persistence.getLocalNeedMoreInfo
import com.example.rubbishcommunity.persistence.getLocalVerifiedEmail
import com.example.rubbishcommunity.service.MqServiceConnection
import com.example.rubbishcommunity.service.MyMqttService
import com.example.rubbishcommunity.ui.container.jumpToBasicInfo
import com.example.rubbishcommunity.ui.container.jumpToReleaseDynamic
import com.example.rubbishcommunity.ui.home.message.MessageFragment
import com.example.rubbishcommunity.ui.home.mine.MineFragment
import com.example.rubbishcommunity.ui.home.search.SearchFragment
import com.example.rubbishcommunity.ui.widget.AddDialog
import com.example.rubbishcommunity.ui.widget.statushelper.StatusBarUtil
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import android.view.KeyEvent.KEYCODE_BACK




class MainActivity : BindingActivity<MainBinding, MainViewModel>() {
	override val clazz: Class<MainViewModel> = MainViewModel::class.java
	override val layRes: Int = R.layout.activity_main
	private var currentFragment: Fragment? = SearchFragment()
	
	private val mqServiceConnection = MqServiceConnection()
	
	
	override fun initBefore() {
	
	}
	
	@SuppressLint("ResourceType")
	override fun initWidget() {
		//状态栏字体黑色
		StatusBarUtil.setStatusTextColor(true, this)
		//不需要验证邮箱和完善信息,初始化home界面
		supportFragmentManager.beginTransaction().apply {
			add(R.id.maincontainer, currentFragment as Fragment)
			commit()
		}
		RxView.clicks(binding.btnAdd).doOnNext {
			showAddDialog()
		}.bindLife()
		
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
		
		//发个消息来测试
		RxView.clicks(binding.btnSendMqttSmg)
			.throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				publishMqMsg("我是mq消息")
/*				mqttPublish(viewModel.mqttClient).doOnSuccess {
					MyApplication.showSuccess("send success")
				}.bindLife()*/
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
		//启动mqtt服务
		bindService(
			Intent(this, MyMqttService::class.java),
			mqServiceConnection,
			Context.BIND_AUTO_CREATE
		)

/*		viewModel.initMqtt()
			.doOnDispose {
				Log.d("AAAAA", "退订流成功")
			}.bindLife()*/
		
		
	}
	
	
	private fun publishMqMsg(string: String) {
		mqServiceConnection.getMqttService().publishMessage(string)
	}
	
	
	private fun replaceFragment(tag: String) {
		if (currentFragment != null) {
			supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
		}
		currentFragment = supportFragmentManager.findFragmentByTag(tag)
		
		if (currentFragment == null) {
			when (tag) {
				"home" ->
					currentFragment = SearchFragment()
				
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
		AddDialog(this, object : AddDialog.OnAddDialogClickListener {
			override fun onDynamicClick() {
				//开启发布动态界面
				jumpToReleaseDynamic(this@MainActivity)
			}
			
			override fun onVoteClick() {
			
			}
			
			override fun onDismiss() {
			
			}
		}).showAbove(binding.btnAdd)
	}

/*    private fun startByRxActivityResult() {
        RxActivityResult.on(this)
            .startIntent(Intent(this, CollectionActivity::class.java))
            .map { result -> result.data() }
            .doOnNext {
                if (targetFragment is FindFragment) {
                    val deleteArray: MutableList<String> = it.getSerializableExtra("deleteArray") as MutableList<String>
                    (targetFragment as FindFragment).updateListFromDeletes(deleteArray)
                }
            }.bindLife()
    }*/
	
	override fun onDestroy() {
		super.onDestroy()
		// 退订MQTT
		unbindService(mqServiceConnection)
/*		mqttUnsubscribe(viewModel.mqttClient)
			.doOnSuccess {
				Log.d("AAAAA", "退订MQTT成功")
			}.bindLife()
		*/
	}
	
	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (keyCode == KEYCODE_BACK) {
			val intent = Intent(Intent.ACTION_MAIN)
			intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
			intent.addCategory(Intent.CATEGORY_HOME)
			startActivity(intent)
			return true
		}
		return super.onKeyDown(keyCode, event)
	}
	
	
	
}
