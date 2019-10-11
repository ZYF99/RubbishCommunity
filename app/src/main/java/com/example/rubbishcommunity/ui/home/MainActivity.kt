package com.example.rubbishcommunity.ui.home


import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import com.example.rubbishcommunity.ui.home.find.FindFragment
import com.example.rubbishcommunity.ui.BindingActivity
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MainBinding
import com.example.rubbishcommunity.ui.container.jumpToReleaseDynamic
import com.example.rubbishcommunity.ui.guide.welcome.WelcomeFragment
import com.example.rubbishcommunity.ui.home.message.MessageFragment
import com.example.rubbishcommunity.ui.home.mine.MineFragment
import com.example.rubbishcommunity.ui.widget.AddDialog
import com.example.rubbishcommunity.ui.widget.statushelper.StatusBarUtil
import com.jakewharton.rxbinding2.view.RxView


class MainActivity : BindingActivity<MainBinding, MainViewModel>() {
	override val clazz: Class<MainViewModel> = MainViewModel::class.java
	override val layRes: Int = R.layout.activity_main
	private var currentFragment: Fragment? = WelcomeFragment()
	
	
	override fun initBefore() {
	}
	
	@SuppressLint("ResourceType")
	override fun initWidget() {
		//状态栏字体黑色
		StatusBarUtil.setStatusTextColor(true, this)
		supportFragmentManager.beginTransaction().apply {
			add(R.id.maincontainer, currentFragment as Fragment)
			commit()
		}
		
		RxView.clicks(binding.btnAdd).doOnNext {
			showAddDialog()
		}.bindLife()
		
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
				"home" ->
					currentFragment = WelcomeFragment()
				
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
	

	
	
}
