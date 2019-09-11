package com.example.rubbishcommunity.base

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

abstract class BaseViewModel(application: Application) :
	AndroidViewModel(application),
	KodeinAware,
	BindLife {
	
	override val kodein: Kodein by lazy { (application as MyApplication).kodein }
	override val compositeDisposable = CompositeDisposable()
	
	
	//fun for ste default value
	fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }
	
	
	//run when this viewModel will be destroyed
	override fun onCleared() {
		compositeDisposable.clear()
		super.onCleared()
	}
	
	
	//resolve error in Observable
	protected fun <T> dealError(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs.doOnError { error ->
				//Toast.makeText(MyApplication.instance, error.toString(), Toast.LENGTH_SHORT).show()
				when (error) {
					//is ServerError -> sendError(ErrorData(ErrorType.SERVER_ERROR, it.code.toString()))
					//is LoginException ->{}
					//else -> sendError(ErrorData(ErrorType.UNEXPECTED))
				}
			}
		}
	}
	
	
}