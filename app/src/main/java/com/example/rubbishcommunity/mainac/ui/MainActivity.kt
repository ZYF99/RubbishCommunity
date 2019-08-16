package com.example.rubbishcommunity.mainac.ui


import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.example.rubbishcommunity.mainac.ui.find.FindFragment
import com.example.fenrir_stage4.mainac.utils.ErrorType
import com.example.fenrir_stage4.mainac.utils.getErrorObs
import com.example.fenrir_stage4.mainac.utils.showNoWifiDialog
import com.example.fenrir_stage4.mainac.utils.showUnexpectedDialog
import com.example.rubbishcommunity.base.BindingActivity
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MainBinding
import com.example.rubbishcommunity.mainac.ui.homepage.HomepageFragment
import com.example.rubbishcommunity.mainac.ui.message.MessageFragment
import com.example.rubbishcommunity.mainac.ui.mine.MineFragment
import com.example.rubbishcommunity.mainac.ui.widget.statushelper.StatusBarUtil
import com.jakewharton.rxbinding2.support.design.widget.RxBottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber
import kotlin.math.min


@Suppress("UNCHECKED_CAST")
class MainActivity : BindingActivity<MainBinding, MainViewModel>() {
    override val clazz: Class<MainViewModel> = MainViewModel::class.java
    override val layRes: Int = R.layout.activity_main
    private var errorDisposable: Disposable? = null
    private var errorDialog: AlertDialog? = null




    private val homepageFragment: HomepageFragment = HomepageFragment()
    private val findFragment: FindFragment? = FindFragment()
    private val messageFragment: MessageFragment = MessageFragment()
    private val mineFragment: MineFragment = MineFragment()

    private var currentFragment: Fragment? = homepageFragment
    private var targetFragment: Fragment? = null




    override fun initBefore() {

    }

    @SuppressLint("ResourceType")
    override fun initWidget() {
        //状态栏字体黑色
        StatusBarUtil.setStatusTextColor(true, this)
        supportFragmentManager.beginTransaction().apply {
            add(R.id.maincontainer, homepageFragment as Fragment)
            add(R.id.maincontainer, findFragment as Fragment)
            add(R.id.maincontainer, messageFragment as Fragment)
            add(R.id.maincontainer, mineFragment as Fragment)
            hide(homepageFragment)
            hide(mineFragment)
            hide(messageFragment)
            hide(findFragment)
            show(homepageFragment)
            commit()
        }


        RxBottomNavigationView.itemSelections(binding.bottomnavigation)
            .doOnNext {
                when (it.itemId) {
                    R.id.navigation_home -> {
                        changeTab(0)
                    }
                    R.id.navigation_find -> {
                        changeTab(1)
                    }
                    R.id.navigation_message -> {
                        changeTab(2)
                    }
                    R.id.navigation_mine -> {
                        changeTab(3)
                    }
                }
            }.bindLife()
        //resolve error
        handleError()
    }

    override fun initData() {

    }

    @SuppressLint("NewApi")
    private fun changeTab(tab: Int) {
        when (tab) {
            0 -> {
                targetFragment = homepageFragment
            }
            1 -> {
                targetFragment = findFragment
            }
            2 -> {
                targetFragment = messageFragment
            }
            3 -> {
                targetFragment = mineFragment
            }
        }

        if(currentFragment!!::class != targetFragment!!::class){
            supportFragmentManager.beginTransaction().apply {
                hide(currentFragment!!)
                show(targetFragment!!)
                currentFragment = targetFragment
                setTransition(TRANSIT_FRAGMENT_FADE)
                commit()
            }

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
