package com.example.rubbishcommunity.base


import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein


abstract class BindingActivity<Bind : ViewDataBinding, VM : AndroidViewModel>
    : AppCompatActivity(),KodeinAware, BindLife {

    override val kodein by closestKodein()

    abstract val clazz: Class<VM>
    abstract val layRes: Int

    val viewModel: VM by lazy {
        ViewModelProviders.of(
            this
            , ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(clazz)
    }

    lateinit var binding: Bind

    override val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layRes)
        initBefore()
        initWidget()
        initData()
    }


    //something init before initWidget
    abstract fun initBefore()

    //widget init
    abstract fun initWidget()

    //data init
    abstract fun initData()


    //Fragment's destroy function
    override fun onDestroy() {
        destroyDisposable()
        super.onDestroy()
    }


    //ext

    protected fun <T> LiveData<T>.observe(observer: (T?) -> Unit) where T : Any =
        observe(this@BindingActivity, Observer<T> { v -> observer(v) })

    protected fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) {
        this.observe(this@BindingActivity, Observer {
            if (it != null) {
                observer(it)
            }
        })
    }
    
    
    override fun onBackPressed() {
        //得到当前activity下的所有Fragment
        val fragments = supportFragmentManager.fragments
        //判断是否为空
        if (fragments.size > 0) {
            for (fragment in fragments) {
                //判断是否为我们能处理的fragment类型
                if (fragment is BindingFragment<*, *>) {
                    //判断是否拦截了返回按钮事件
                    if (fragment.onBackPressed()) {
                        //如果被Fragment处理了就直接return
                        return
                    }
                }
            }
        }
        //没有Fragment，没有处理 就结束活动
        super.onBackPressed()
        finish()
    }

    //hide keyBoard
    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (imm!!.isActive)
            imm.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
    }


}
