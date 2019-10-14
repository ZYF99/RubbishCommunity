package com.example.rubbishcommunity.ui.container


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.rubbishcommunity.ui.BindingActivity
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ContainerBinding
import com.example.rubbishcommunity.model.Comment
import com.example.rubbishcommunity.ui.SoftObservableFragment
import com.example.rubbishcommunity.ui.guide.basicinfo.BasicInfoFragment
import com.example.rubbishcommunity.ui.guide.login.LoginFragment
import com.example.rubbishcommunity.ui.guide.register.RegisterFragment
import com.example.rubbishcommunity.ui.home.find.dynamic.detail.DynamicDetailFragment
import com.example.rubbishcommunity.ui.home.find.dynamic.detail.innercomment.InnerCommentFragment
import com.example.rubbishcommunity.ui.release.dynamic.ReleaseDynamicFragment
import com.example.rubbishcommunity.ui.widget.statushelper.StatusBarUtil
import com.example.rubbishcommunity.utils.SoftKeyBroadManager
import java.io.Serializable


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
	private var currentFragment: SoftObservableFragment? = SoftObservableFragment()
	
	
	override fun initBefore() {
		if (intent.getSerializableExtra("tag") == null) {
			//刚进入app时
			
			replaceFragment("register")
			
			
		} else {
			replaceFragment(intent.getSerializableExtra("tag") as String)
		}
	}
	
	@SuppressLint("ResourceType")
	override fun initWidget() {
		//状态栏字体黑色
		StatusBarUtil.setStatusTextColor(true, this)
		binding.vm = viewModel
		handleError()
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
				"login" -> //登陆界面
					LoginFragment()
				"register" -> //注册界面
					RegisterFragment()
				"basicInfo" -> //基本信息界面
					BasicInfoFragment()
				"releaseDynamic" -> // 发布动态界面
					ReleaseDynamicFragment()
				"dynamicDetail" -> //动态详情界面
					DynamicDetailFragment()
				"innerComment" -> //内部评论列表界面
					InnerCommentFragment()
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
	val bundle = Bundle()
	bundle.putString("tag", "login")
	
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至完善基本信息的界面
fun jumpToBasicInfo(context: Context) {
	val bundle = Bundle()
	bundle.putString("tag", "basicInfo")
	
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至发布动态界面界面
fun jumpToReleaseDynamic(context: Context) {
	val bundle = Bundle()
	bundle.putString("tag", "releaseDynamic")
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}

//跳转至动态详情界面
fun jumpToDynamicDetail(context: Context, dynamicId: String) {
	val bundle = Bundle()
	bundle.putString("tag", "dynamicDetail")
	bundle.putSerializable(
		"dynamicId",
		dynamicId as Serializable
	)
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}


//跳转至内部评论列表界面
fun jumpToInnerComment(context: Context, commentList: List<Comment>) {
	val bundle = Bundle()
	bundle.putString("tag", "innerComment")
	bundle.putSerializable(
		"commentList",
		commentList as Serializable
	)
	context.startActivity(Intent(context, ContainerActivity::class.java).putExtras(bundle))
}



