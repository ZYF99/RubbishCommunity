package com.example.rubbishcommunity.ui.container

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.base.BindingActivity
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ContainerBinding
import com.example.rubbishcommunity.model.api.News
import com.example.rubbishcommunity.model.api.machine.FetchMachineInfoResultModel
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.model.api.news.NewsResult
import com.example.rubbishcommunity.persistence.saveLoginState
import com.example.rubbishcommunity.ui.base.SoftObservableFragment
import com.example.rubbishcommunity.ui.guide.basicinfo.BasicInfoFragment
import com.example.rubbishcommunity.ui.guide.login.LoginFragment
import com.example.rubbishcommunity.ui.guide.password.PasswordFragment
import com.example.rubbishcommunity.ui.guide.register.RegisterFragment
import com.example.rubbishcommunity.ui.guide.welcome.WelcomeFragment
import com.example.rubbishcommunity.ui.home.find.moment.detail.MomentDetailFragment
import com.example.rubbishcommunity.ui.home.message.chat.ChatFragment
import com.example.rubbishcommunity.ui.home.homepage.newsdetail.NewsDetailFragment
import com.example.rubbishcommunity.ui.search.SearchFragment
import com.example.rubbishcommunity.ui.search.cameraSearch.CameraSearchFragment
import com.example.rubbishcommunity.ui.home.mine.editinfo.EditInfoFragment
import com.example.rubbishcommunity.ui.home.mine.machinedetail.MachineDetailFragment
import com.example.rubbishcommunity.ui.release.moments.MomentsType
import com.example.rubbishcommunity.ui.release.moments.ReleaseMomentFragment
import com.example.rubbishcommunity.ui.userinfo.UserInfoFragment
import com.example.rubbishcommunity.ui.widget.statushelper.StatusBarUtil
import com.example.rubbishcommunity.utils.SoftKeyBroadManager
import com.example.rubbishcommunity.utils.globalGson
import rx_activity_result2.RxActivityResult

class ContainerActivity : BindingActivity<ContainerBinding, ContainerViewModel>(),
	SoftKeyBroadManager.SoftKeyboardStateListener {
	
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
		binding.root.scrollTo(0, keyboardHeightInPx)
	}
	
	override fun onSoftKeyboardClosed() {
		binding.root.scrollTo(0, 0)
	}
	
	//跳转至register
	fun replaceRegister() {
		replaceFragment("register")
	}
	
	//跳转至login
	fun replaceLogin() {
		replaceFragment("login")
	}
	
	override val clazz: Class<ContainerViewModel> = ContainerViewModel::class.java
	override val layRes: Int = R.layout.activity_container
	private var currentFragment: SoftObservableFragment? =
		SoftObservableFragment()
	
	
	override fun initBefore() {
		binding.vm = viewModel
	}
	
	@SuppressLint("ResourceType")
	override fun initWidget() {
		//状态栏字体黑色
		StatusBarUtil.setStatusTextColor(true, this)
		binding.vm = viewModel
		handleError()
		if (intent.getSerializableExtra("tag") == null) {
			//刚进入app时
			replaceFragment("welcome")
		} else {
			replaceFragment(intent.getSerializableExtra("tag") as String)
		}
		
	}
	
	override fun initData() {
	
	}
	
	private fun replaceFragment(tag: String) {
		if (currentFragment != null) {
			supportFragmentManager.beginTransaction().hide(currentFragment!!).commit()
		}
		currentFragment = supportFragmentManager.findFragmentByTag(tag) as SoftObservableFragment?
		
		if (currentFragment == null) {
			currentFragment = when (tag) {
				"welcome" -> //欢迎界面
					WelcomeFragment()
				"login" -> //登陆界面
					LoginFragment()
				"register" -> //注册界面
					RegisterFragment()
				"news" -> //新闻界面
					NewsDetailFragment()
				"search" -> //搜索界面
					SearchFragment()
				"CameraSearch" -> //拍照搜索界面
					CameraSearchFragment()
				"password" -> //修改密码界面
					PasswordFragment()
				"basicInfo" -> //基本信息界面
					BasicInfoFragment()
				"editUserInfo" -> //修改信息界面
					EditInfoFragment()
				"releaseMoments" -> // 发布动态界面
					ReleaseMomentFragment()
				"momentDetail" -> //动态详情界面
					MomentDetailFragment()
				"machineDetail" -> //机器详情界面
					MachineDetailFragment()
				"chat" -> //聊天界面
					ChatFragment()
				"userInfo" -> //用户主页界面
					UserInfoFragment()
				else -> SoftObservableFragment()
			}
			supportFragmentManager.beginTransaction()
				.add(R.id.guideContainer, currentFragment!!, tag).commit()
		} else {
			supportFragmentManager.beginTransaction().show(currentFragment!!).commit()
		}
	}
}

//跳转至登陆界面
fun jumpToLogin(context: Context) {
	saveLoginState(false)
	val bundle = Bundle()
	bundle.putString("tag", "login")
	context.startActivity(
		Intent(
			context,
			ContainerActivity::class.java
		).setFlags(
			Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
		).putExtras(bundle)
	)
}

//跳转至注册界面
fun jumpToRegister(context: Context) {
	val bundle = Bundle()
	bundle.putString("tag", "register")
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至修改密码的界面
fun jumpToPassword(context: Context) {
	val bundle = Bundle()
	bundle.putString("tag", "password")
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//从'我的'界面跳转至'修改信息'界面
fun jumpToEditInfo(context: Context) {
	val bundle = Bundle()
	bundle.putString("tag", "editUserInfo")
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至完善基本信息的界面
fun jumpToBasicInfo(context: Context) {
	val bundle = Bundle()
	bundle.putString("tag", "basicInfo")
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至文章详情界面
fun jumpToNewsDetail(context: Context, news: NewsResult.News?) {
	val bundle = Bundle()
	bundle.putString("tag", "news")
	bundle.putSerializable(
		"news",
		globalGson.toJson(news)
	)
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至搜索界面
fun jumpToSearch(context: Context) {
	val bundle = Bundle()
	bundle.putString("tag", "search")
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至拍照搜索界面
fun jumpToCameraSearch(fragment: Fragment) {
	val bundle = Bundle()
	bundle.putString("tag", "CameraSearch")
	RxActivityResult.on(fragment)
		.startIntent(Intent(fragment.context, ContainerActivity::class.java).putExtras(bundle))
		.doOnNext {
			if (it.resultCode() == 1) {
				(fragment.activity as ContainerActivity).viewModel.run {
					MyApplication.showSuccess(it.data().getStringExtra("searchKey") ?: "")
					keyWordFromCamera.postValue(
						it.data().getStringExtra("searchKey")
					)
				}
			}
			
		}.subscribe()
	
}


//跳转至发布动态界面界面
fun jumpToReleaseMoments(context: Context, @MomentsType type: String) {
	val bundle = Bundle()
	bundle.putString("tag", "releaseMoments")
	bundle.putString("moments_type", type)
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至动态详情界面
fun jumpToMomentDetail(context: Context, moment: MomentContent) {
	val bundle = Bundle()
	bundle.putString("tag", "momentDetail")
	bundle.putSerializable(
		"moment",
		globalGson.toJson(moment)
	)
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至动态详情界面
fun jumpToUserInfo(context: Context, openId: String) {
	val bundle = Bundle()
	bundle.putString("tag", "userInfo")
	bundle.putString(
		"openId",
		openId
	)
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至机器详情界面
fun jumpToMachineDetail(context: Context, machineInfo: FetchMachineInfoResultModel.MachineHeathInfo) {
	val bundle = Bundle()
	bundle.putString("tag", "machineDetail")
	bundle.putString("machineInfo", globalGson.toJson(machineInfo))
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至聊天界面
fun jumpToChat(context: Context, openId: String) {
	val bundle = Bundle()
	bundle.putString("tag", "chat")
	bundle.putString("uid", openId)
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}




