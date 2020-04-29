package com.example.rubbishcommunity.ui.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.manager.UiError
import com.example.rubbishcommunity.service.MQNotifyData
import com.example.rubbishcommunity.service.getMQNotifyObs
import com.example.rubbishcommunity.ui.utils.*
import com.example.rubbishcommunity.utils.BindLife
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.closestKodein
import timber.log.Timber


abstract class BindingFragment<Bind : ViewDataBinding, VM : BaseViewModel>
constructor(
	private val clazz: Class<VM>,
	private val bindingCreator: (LayoutInflater, ViewGroup?) -> Bind
) : SoftObservableFragment(), KodeinAware, BindLife{
	
	var isVisibleToUser = true
	var alertDialog: AlertDialog? = null
	
	constructor(clazz: Class<VM>, @LayoutRes layoutRes: Int) : this(clazz, { inflater, group ->
		DataBindingUtil.inflate(inflater, layoutRes, group, false)
	})
	
	protected open val viewModel: VM by lazy {
		ViewModelProviders.of(this).get(clazz)
	}
	override val kodein by closestKodein()
	
	lateinit var binding: Bind
	
	override val compositeDisposable = CompositeDisposable()
	private var notifyDisposable: Disposable? = null //MQ消息流
	
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
		viewModel.isLoading.observeNonNull {
			if (it) {
				if (alertDialog == null) {
					alertDialog = AlertDialog.Builder(context!!)
						.setView(R.layout.dialog_loading)
						.setCancelable(false)
						.create()
				}
				alertDialog?.showContent()
			} else {
				if (alertDialog != null) {
					alertDialog?.dismiss()
				}
			}
			
		}
		//退出点击事件
		view.findViewById<Toolbar>(R.id.toolbar)
			?.setNavigationOnClickListener {
				activity?.finish()
			}
		
		
		if (!viewModel.vmInit) {
			initData()
			handleMQNotifyMessage()
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
	
	fun AlertDialog.showContent() {
		show()
		val params = this.window?.attributes
		params?.width = context.dp2px(200f)
		params?.height = context.dp2px(200f)
		this.window?.attributes = params;
	}
	

	//实际'MQ消息'处理者
	private fun handleMQNotifyMessage() {
		notifyDisposable = getMQNotifyObs()
			.observeOn(AndroidSchedulers.mainThread())
			.doOnNext {
					onMQMessageArrived(it)
			}.subscribe({}, { Timber.e(it) })
	}
	
	//关于MQ消息的处理方法，需要实现的Fragment自行重写
	protected open fun onMQMessageArrived(mqNotifyData: MQNotifyData) {
	
	}
	
}


