package com.example.rubbishcommunity.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.manager.UiError
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import com.example.rubbishcommunity.utils.BindLife
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein


abstract class BindingFragment<Bind : ViewDataBinding, VM : BaseViewModel>
constructor(
	private val clazz: Class<VM>,
	private val bindingCreator: (LayoutInflater, ViewGroup?) -> Bind
) : SoftObservableFragment(), KodeinAware,
	BindLife {
	
	constructor(clazz: Class<VM>, @LayoutRes layoutRes: Int) : this(clazz, { inflater, group ->
		DataBindingUtil.inflate(inflater, layoutRes, group, false)
	})
	
	protected open val viewModel: VM by lazy {
		ViewModelProviders.of(this).get(clazz)
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
		binding.lifecycleOwner = this
		initBefore()
		initWidget()
		//退出点击事件
		view.findViewById<Toolbar>(R.id.toolbar)
			?.setNavigationOnClickListener {
				activity?.finish()
			}
		if (!viewModel.vmInit) {
			initData()
			viewModel.vmInit = true
		}
		
	}
	
	//something init before initWidget
	abstract fun initBefore()
	
	//widget init
	abstract fun initWidget()
	
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
		(viewModel as BaseViewModel).destroyDisposable()
		super.onDestroy()
	}
	
	//on backKey pressed
	open fun onBackPressed(): Boolean {
		return false
	}
	
	
	//check network
	protected fun isNetworkAvailable() =
		(context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isConnected
			?: false
	
	
	fun Context.checkNet(): Completable =
		Completable.create { emitter ->
			if (!isNetworkAvailable()) emitter.onError(UiError(context!!.getString(R.string.net_unavailable)))
			else emitter.onComplete()
		}
	
	fun BindingFragment<*, *>.showNetErrorSnackBar() {
		sendError(
			ErrorType.UI_ERROR,
			"没有网络"
		)
	}
	
}


