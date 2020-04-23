package com.example.rubbishcommunity.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.utils.BindLife
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

abstract class BaseViewModel(application: Application) :
	AndroidViewModel(application),
	KodeinAware,
	BindLife {
	val isLoading = MutableLiveData(false)
	override val kodein: Kodein by lazy { (application as MyApplication).kodein }
	override val compositeDisposable = CompositeDisposable()
	var vmInit = false
	
	//fun for set default value
	fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }
	
	
	//run when this viewModel will be destroyed
	override fun onCleared() {
		compositeDisposable.clear()
		super.onCleared()
	}
	
	protected fun <T> Single<T>.doOnApiSuccess(action: (T) -> Unit) =
		switchThread()
			.catchApiError()
			.doOnSuccess { action.invoke(it) }
			.bindLife()
	
	protected fun <T> Single<T>.dealLoading() =
		doOnSubscribe { isLoading.postValue(true) }
			.doFinally { isLoading.postValue(false) }
	
}