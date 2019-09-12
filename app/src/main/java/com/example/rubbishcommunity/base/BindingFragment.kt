package com.example.rubbishcommunity.base


import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.rubbishcommunity.R
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein

abstract class BindingFragment<Bind : ViewDataBinding, VM : AndroidViewModel>
constructor(
    private val clazz: Class<VM>,
    private val bindingCreator: (LayoutInflater, ViewGroup?) -> Bind
) : Fragment(), KodeinAware, BindLife {

    constructor(clazz: Class<VM>, @LayoutRes layoutRes: Int) : this(clazz, { inflater, group ->
        DataBindingUtil.inflate(inflater, layoutRes, group, false)
    })

    val viewModel: VM by lazy {
        ViewModelProviders.of(
            this
            , ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        ).get(clazz)
    }
    override val kodein by closestKodein()

    lateinit var binding: Bind

    override val compositeDisposable = CompositeDisposable()


    //create view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = bindingCreator.invoke(layoutInflater, container)
        return binding.root
    }


    //do when view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            initBefore()
            initWidget(view)
            initData()
    }


    //something init before initWidget
    abstract fun initBefore()

    //widget init
    abstract fun initWidget(view: View)

    //data init
    abstract fun initData()


    //ext

    protected fun <T> LiveData<T>.observe(observer: (T?) -> Unit) where T : Any =
        observe(this@BindingFragment, Observer<T> { v -> observer(v) })

    protected fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) {
        this.observe(this@BindingFragment, Observer {
            if (it != null) {
                observer(it)
            }
        })
    }


    //Fragment's destroy function
    override fun onDestroy() {
        destroyDisposable()
        super.onDestroy()
    }

    
    open fun onBackPressed():Boolean{
        return false
    }

    //hide keyBoard
    fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (imm!!.isActive)
            imm.hideSoftInputFromWindow(
                (context as Activity).currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
    }

    protected fun isNetworkAvailable() =
        (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isConnected ?: false


}


