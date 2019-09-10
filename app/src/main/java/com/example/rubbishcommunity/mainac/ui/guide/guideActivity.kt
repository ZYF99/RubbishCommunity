package com.example.rubbishcommunity.mainac.ui.guide


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.example.fenrir_stage4.mainac.utils.ErrorType
import com.example.fenrir_stage4.mainac.utils.getErrorObs
import com.example.fenrir_stage4.mainac.utils.showNoWifiDialog
import com.example.fenrir_stage4.mainac.utils.showUnexpectedDialog
import com.example.rubbishcommunity.base.BindingActivity
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.GuideBinding
import com.example.rubbishcommunity.mainac.ui.guide.login.LoginFragment
import com.example.rubbishcommunity.mainac.ui.guide.register.RegisterFragment
import com.example.rubbishcommunity.mainac.ui.widget.statushelper.StatusBarUtil
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber


@Suppress("UNCHECKED_CAST")
class guideActivity : BindingActivity<GuideBinding, GuideViewModel>() {
    override val clazz: Class<GuideViewModel> = GuideViewModel::class.java
    override val layRes: Int = R.layout.activity_guide
    
    private var errorDisposable: Disposable? = null
    private var errorDialog: AlertDialog? = null

    private val loginFragment: LoginFragment =
        LoginFragment()
    private val registerFragment: RegisterFragment =
        RegisterFragment()
    
    override fun initBefore() {

    }

    @SuppressLint("ResourceType")
    override fun initWidget() {
        //状态栏字体黑色
        StatusBarUtil.setStatusTextColor(true, this)
        
        supportFragmentManager.beginTransaction().apply {
            add(R.id.logincontainer, loginFragment as Fragment)
            add(R.id.logincontainer, registerFragment as Fragment)
            hide(registerFragment)
            commit()
        }


/*        RxView.clicks(binding.btnAdd).doOnNext {

            Toast.makeText(this, "添加", Toast.LENGTH_SHORT).show()

        }.bindLife()*/

        
/*
        RxBottomNavigationView.itemSelections(binding.bottomnavigation)
            .doOnNext {

            }.bindLife()
*/


        //resolve error
        handleError()
    }

    override fun initData() {

    }

    @SuppressLint("NewApi")
    private fun transitToFrag(tab: Int) {
            supportFragmentManager.beginTransaction().apply {
                if(tab == 0){
                    hide(loginFragment)
                    show(registerFragment)
                }else{
                    hide(registerFragment)
                    show(loginFragment)
                }
                setTransition(TRANSIT_FRAGMENT_FADE)
                commit()
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
