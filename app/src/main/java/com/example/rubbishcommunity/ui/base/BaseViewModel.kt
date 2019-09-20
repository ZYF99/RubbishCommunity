package com.example.rubbishcommunity.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.utils.ErrorData
import com.example.rubbishcommunity.utils.ErrorType
import com.example.rubbishcommunity.utils.sendError
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
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
	
	

	
	
}