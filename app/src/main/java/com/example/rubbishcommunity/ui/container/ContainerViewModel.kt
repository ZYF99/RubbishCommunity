package com.example.rubbishcommunity.ui.container

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.ui.base.BaseViewModel


class ContainerViewModel(application: Application) : BaseViewModel(application) {

	//专属于Camera识别界面finish到search的时候
	val shouldSearchCameraResult = MutableLiveData(false)
	//Camera界面传回的关键字
	val keyWordFromCamera = MutableLiveData("")
	
	fun init() {
	
	}
	
}